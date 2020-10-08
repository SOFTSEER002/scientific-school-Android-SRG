package com.jeannypr.scientificstudy.Chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.activity.BaseActivity;
import com.jeannypr.scientificstudy.Chat.ChatViewModel;
import com.jeannypr.scientificstudy.Chat.adapter.ChatListAdapter;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatMessageBean;
import com.jeannypr.scientificstudy.Chat.model.ChatMessageModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.UI.EndlessRecyclerViewScrollListener;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityChatBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO:integrate socket live streaming of messages
public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> implements Observer {

    private ActionBar mActionBar;
    private static final String TAG = "ChatActivity";
    private RecyclerView mMessageRecycler;
    private ChatListAdapter mMessageAdapter;
    List<ChatMessageModel> messageList;
    UserPreference userPreference;
    EditText mEtMessage;
    MediaPlayer mp;
    int userId;
    int chatRoomId;
    String schoolCode;
    ChatService mService;
    Context mContext;
    private Boolean isConnected = true;
    private static final int TYPING_TIMER_LENGTH = 600;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;
    Gson gson;
    ChatQueue queue;
    boolean isGroupChat = false;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    int PAGE_SIZE = 200;
    int skip = 0;
    int take = 50;
    boolean isLoading = false;
    int otherUserId;
    private Socket mSocket;
    boolean IsAllTeacherGroup = false;
    int ClassId;
    boolean IsClass = false;
    private ChatViewModel mViewModel;
    private ActivityChatBinding mViewBinding;

    {
        try {
            mSocket = IO.socket(ApiConstants.CHAT_BASE_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = UserPreference.getInstance(this);
        mContext = this;

        userId = userPreference.getUserId();
        mUsername = userPreference.getUserName();
        schoolCode = userPreference.getSchoolCode();
        gson = new Gson();
//        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        chatRoomId = intent.getIntExtra("chatRoomId", 0);
        if (chatRoomId != -1) {
            userPreference.SetCurrentChatRoomId(chatRoomId);
        }
        String chatRoomName = intent.getStringExtra("chatRoomName");

        if (intent.hasExtra("isGroup")) {
            isGroupChat = intent.getBooleanExtra("isGroup", false);
        }

        if (intent.hasExtra("otherUserId")) {
            otherUserId = intent.getIntExtra("otherUserId", 0);
        }

        if (intent.hasExtra("IsAllTeacherGroup")) {
            IsAllTeacherGroup = intent.getBooleanExtra("IsAllTeacherGroup", false);
        }

        if (intent.hasExtra("IsClass")) {
            IsClass = intent.getBooleanExtra("IsClass", false);
            ClassId = intent.getIntExtra("ClassId", 0);
        }

        queue = new ChatQueue();
        queue.addObserver(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        Drawable drawable = getResources().getDrawable(R.drawable.profile);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));

        mActionBar.setLogo(newdrawable);
        mActionBar.setTitle(chatRoomName);
        mActionBar.setSubtitle("...");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        FloatingActionButton button_chatbox_send = findViewById(R.id.button_chatbox_send);

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend(mEtMessage.getText().toString().trim());
            }
        });

        mEtMessage = findViewById(R.id.edittext_chatbox);
        mEtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                attemptSend(mEtMessage.getText().toString().trim());
                return true;
            }
        });
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing", chatRoomId, mUsername);
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mService = new DataRepo<>(ChatService.class, mContext, ApiConstants.CHAT_BASE_URL).getService();

        //final Gson gson = new Gson();

        setUpSocket();
        checkUserStatus();
        if (chatRoomId != 0) {

            messageList = new ArrayList<>();
            mMessageRecycler = findViewById(R.id.reyclerview_message_list);
            mMessageAdapter = new ChatListAdapter(this, messageList, isGroupChat);

            mMessageRecycler.setAdapter(mMessageAdapter);
            layoutManager = new LinearLayoutManager(this);
            // layoutManager.setItemPrefetchEnabled(true);
            layoutManager.setInitialPrefetchItemCount(take / 3);

            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            mMessageRecycler.setLayoutManager(layoutManager);

            mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        scrollToBottom();
                    }
                }
            });


            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    skip = page * take;
                    loadChats(false, false);
                }
            };
            //   scrollListener.enableScrollUpPaging();
            // Adds the scroll listener to RecyclerView
            mMessageRecycler.addOnScrollListener(scrollListener);

            mSocket.emit("joinRoom", chatRoomId, userPreference.getUserName(), userId);
            loadChats(true, true);
        }

        if (userPreference.getUserData().getRoleTitle().equals(Constants.Role.PARENT) && !userPreference.getSchoolData().getCanParentReplyInChat()) {
            findViewById(R.id.layout_chatbox).setVisibility(View.GONE);
            findViewById(R.id.cantReplyToChatMsg).setVisibility(View.VISIBLE);
        }
    }

    @NotNull
    @Override
    public ChatViewModel getViewModel() {
        mViewModel = new ChatViewModel();
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        return mViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
        if (timer != null) {
            timer.cancel();
        }

    }

    private void setUpSocket() {

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.on("message read", onMessageRead);
        mSocket.connect();


    }

    private void checkUserStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        mService.IsUserOnline(otherUserId, userId, schoolCode).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    JsonObject obj = response.body();
                                    if (obj != null && obj.has("isOnline")) {
                                        boolean isOnline = obj.get("isOnline").getAsBoolean();
                                        setStatus(isOnline ? "online" : "offline");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });

                        mService.IsUserOnline(otherUserId, userId, schoolCode).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    JsonObject obj = response.body();
                                    if (obj != null && obj.has("isOnline")) {
                                        boolean isOnline = obj.get("isOnline").getAsBoolean();
                                        setStatus(isOnline ? "online" : "offline");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });

                        //periodically sends data to the server for status updation. this way the user is sure to get the
                        //read status with a lag of 30 sec.
                        notifyUnreadMessage();

                    }
                };

                timer = new Timer();
                timer.schedule(timerTask, 0, 30 * 1000);
            }
        });
    }

    private void scrollToBottom() {
        // mMessageRecycler.scrollToPosition(0);
        //mMessageAdapter.getItemCount() - 1
        mMessageRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageRecycler.smoothScrollToPosition(0);
            }
        }, 200);
    }

    private void notifyUnreadMessage() {
        if (!isGroupChat) {
            final Collection<String> unreadMesages = new ArrayList<>();
            for (ChatMessageModel m : messageList) {
                if (!m.IsRead && m.SentBy != userId) {
                    unreadMesages.add(String.valueOf(m.Id));
                }
            }

            if (unreadMesages.size() > 0) {
                String messageIds = Utility.Join(",", unreadMesages);

                mService.NotifyMessageRead(messageIds, schoolCode).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (call.isExecuted()) {
                            try {
                                JsonObject res = response.body();
                                if (res.has("status")) {
                                    boolean isSaved = res.get("status").getAsBoolean();
                                    if (isSaved) {
                                        for (String msgId : unreadMesages) {
                                            mSocket.emit("message read", chatRoomId, Integer.parseInt(msgId));
                                        }
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        }
    }


    /**
     * Method to load chats dynamically.
     * <p>
     * loads the chats based on the values of skip and take
     *
     * @param reset if true, it will reload the data and scroll to bottom
     * @see RecyclerView.LayoutManager#scrollToPosition(int)
     */
    private void loadChats(final Boolean reset, final boolean isFirstTime) {

        isLoading = true;
        mService.GetChatMessages(chatRoomId, schoolCode, userId, userPreference.getUserData().getRoleTitle(), skip, take).enqueue(new Callback<ChatMessageBean>() {
            @Override
            public void onResponse(Call<ChatMessageBean> call, Response<ChatMessageBean> response) {
                ChatMessageBean resp = response.body();
                // int oldMsgsCount = 0;

                List<ChatMessageModel> messages = resp.getChatMessages();
                if (messages != null && messages.size() > 0) {
                    if (reset) {
                        // oldMsgsCount = messageList.size();
                        messageList.clear();
                    }
                    if (PAGE_SIZE == 0) {
                        PAGE_SIZE = resp.getTotalMessages();
                    }
                    messageList.addAll(messages);

                    mMessageAdapter.notifyDataSetChanged();
                    if (isFirstTime)
                        scrollToBottom();

                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<ChatMessageBean> call, Throwable t) {
                isLoading = false;
            }
        });
    }

    private void addMessage(final ChatMessageModel messageModel) {

        messageList.add(0, messageModel);
        mMessageAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private String subTitle;

    private void setStatus(String status) {
        mActionBar.setSubtitle(status);
    }

    private void addTyping(String username) {
        String sub = mActionBar.getSubtitle().toString();
        subTitle = sub.equals("typing...") ? subTitle : sub;
        mActionBar.setSubtitle("typing...");
    }

    private void removeTyping() {
        mActionBar.setSubtitle(subTitle);
        mTyping = false;
    }

    private void attemptSend(String message) {

        // final String message = mEtMessage.getText().toString().trim();
        ChatMessageModel messageModel;
        if (message != null && !message.equals("")) {
            messageModel = new ChatMessageModel();
            messageModel.Message = message;
            messageModel.SentBy = userPreference.getUserId();
            messageModel.UserName = userPreference.getUserData().getFirstName();
            messageModel.ChatRoomId = chatRoomId;
            messageModel.SentOn = System.currentTimeMillis();

            messageModel.IsGroup = isGroupChat;
            if (isGroupChat) {
                if (IsClass) {
                    messageModel.IsClass = true;
                    messageModel.GroupOrReceiverId = ClassId;
                } else if (IsAllTeacherGroup) {
                    messageModel.IsAllTeacherGroup = true;
                }

            } else {
                messageModel.GroupOrReceiverId = otherUserId;
            }

            mEtMessage.setText("");
            if (TextUtils.isEmpty(message)) {
                mEtMessage.requestFocus();
                return;
            }

            addMessage(messageModel);
            queue.add(messageModel);
            // scrollToBottom();
        }
    }

    private void leave() {
        mUsername = null;
        mSocket.disconnect();
        mSocket.connect();
    }

    private void addLog(String message) {
        //TODO: option to add log in the chat area
       /* mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);*/
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    //region Socket Listeners
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (queue.size() > 0) {
                        queue.process();
                    }

                    if (!isConnected) {
                        mSocket.emit("joinRoom", chatRoomId, userPreference.getUserName(), userId);
                        //bing last few messages again.
                        loadChats(true, false);
                        Toast.makeText(mContext, R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(mContext, R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    Toast.makeText(mContext, R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onMessageRead = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int roomId = (int) args[0];
                    int msgId = (int) args[1];

                    if (roomId == chatRoomId && msgId != 0) {

                        //update the UI for read message
                        for (ChatMessageModel m : messageList) {
                            if (m.Id == msgId) {
                                m.IsRead = true;
                                m.IsSent = true;
                                mMessageAdapter.onItemChange(m);
                                break;
                            }
                        }

                        scrollToBottom();
                    }
                }
            });
        }
    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ChatMessageModel message;
                    int roomId = 0;
                    try {
                        roomId = (int) args[0];
                        String msgJson = args[1].toString();
                        message = gson.fromJson(msgJson, ChatMessageModel.class);
                        if (roomId == chatRoomId) {
                            removeTyping();
                            addMessage(message);

                            if (!isGroupChat) {
                                //call the server to update the status of this message
                                notifyUnreadMessage();
                            }

                            PlayNotification();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }


                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int roomId;
                    String userName;
                    int otherRoomUserId;
                    try {
                        otherRoomUserId = data.getInt("userId");
                        roomId = data.getInt("roomId");
                        if (!isGroupChat && roomId == chatRoomId && otherRoomUserId == otherRoomUserId) {
                            setStatus("online");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int roomId;
                    String userName;
                    int otherRoomUserId;
                    try {
                        otherRoomUserId = data.getInt("userId");
                        roomId = data.getInt("roomId");
                        if (!isGroupChat && roomId == chatRoomId && otherRoomUserId == otherRoomUserId) {
                            setStatus("offline");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    removeTyping();
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        addTyping((args[0]).toString());
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeTyping();
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing", chatRoomId, mUsername);
        }
    };

    //endregion

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onPause() {
        mp.stop();
        mp.release();
        super.onPause();
    }

    @Override
    public void onResume() {
        mp = MediaPlayer.create(this, R.raw.chat_continue);
        mp.setVolume(1.0f, 1.0f);
        mp.setLooping(false);
        super.onResume();
    }


    protected void PlayNotification() {

        try {
            if (mp != null) {
                mp.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(final ChatMessageModel messageModel) {

        mService.SendChatMessage(messageModel, schoolCode).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject data = response.body();
                if (data != null && data.has("Id")) {
                    int id = data.get("Id").getAsInt();
                    if (data.has("lastMessageTime")) {
                        long timestamp = data.get("lastMessageTime").getAsLong();
                        messageModel.SentOn = timestamp;
                    }
                    if (id != 0) {

                        messageModel.Id = id;
                        if (mSocket.connected()) {
                            mTyping = false;
                            mSocket.emit("newMessage", chatRoomId, gson.toJson(messageModel));
                        }

                        //update the status of the chat message here locally
                        // this will updat the icon of the message here.
                        messageModel.IsSent = true;
                        messageModel.IsRead = false;
                        mMessageAdapter.onItemChange(messageModel);
                        queue.remove();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {

        final ChatMessageModel messageModel = (ChatMessageModel) arg;
        sendMessage(messageModel);

    }

    /*@Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            final AlertDialog alertDialog = builder.create();

            builder.setTitle("Alert")
                    .setMessage("Are you sure to exit? ")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BaseDashboardActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
    }*/


    private class ChatQueue extends Observable {
        LinkedBlockingQueue<ChatMessageModel> list;

        public ChatQueue() {
            list = new LinkedBlockingQueue<>();

        }

        public int size() {
            return list.size();
        }

        public void process() {
            for (ChatMessageModel data : list) {
                sendMessage(data);
            }
        }

        public void add(ChatMessageModel data) {
            list.add(data);
            setChanged();
            notifyObservers(data);
        }

        public ChatMessageModel remove() {
            try {
                return list.remove();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }


}