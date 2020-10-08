package com.jeannypr.scientificstudy.Chat.adapter;

import android.content.Context;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Chat.model.ChatMessageModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter implements OnItemChangeListener {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<ChatMessageModel> mMessageList;
    private UserPreference mUserPref;
    private boolean isGroupChat;

    public ChatListAdapter(Context context, List<ChatMessageModel> messageList, boolean isGroup) {
        super();
        mContext = context;
        mMessageList = messageList;
        mUserPref = UserPreference.getInstance(context);
        isGroupChat = isGroup;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void updateMessage(ChatMessageModel messageModel){
        int position = mMessageList.indexOf(messageModel);
        notifyItemChanged(position,messageModel);

    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ChatMessageModel message = mMessageList.get(position);

        if (message.SentBy == mUserPref.getUserId()) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        //TODO:rename the files
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_chat_from_me, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_chat_from_others, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        ChatMessageModel message = (ChatMessageModel) mMessageList.get(position);
        message.position = position;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public void onItemChange(ChatMessageModel item) {
        notifyItemChanged(item.position, item);
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView ivMessageStatus;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.chat_msg);
            timeText = itemView.findViewById(R.id.date_view);
            ivMessageStatus = itemView.findViewById(R.id.messageStatus);
        }

        void bind(ChatMessageModel message) {
            messageText.setText(message.Message);
            timeText.setText(Utility.GetChatTime(message.SentOn));



            if(message.IsRead ){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_read_msg, mContext.getApplicationContext().getTheme()));
                } else {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_read_msg));
                }
            }else if(message.IsSent){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_send_msg, mContext.getApplicationContext().getTheme()));
                } else {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_send_msg));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pending_msg, mContext.getApplicationContext().getTheme()));
                } else {
                    ivMessageStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pending_msg));
                }
            }


         //   ivMessageStatus.setImageDrawable(getResource()R.drawable.ic_pending_msg);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, otherUserName;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            //TODO:show name in the group chat
            messageText = itemView.findViewById(R.id.chat_msg);
            timeText = itemView.findViewById(R.id.date_view);
            otherUserName = itemView.findViewById(R.id.otherUserName);
            //profileImage =  itemView.findViewById(R.id.image_message_profile);
        }

        void bind(ChatMessageModel message) {
            messageText.setText(message.Message);
            timeText.setText(Utility.GetChatTime(message.SentOn));
            if (isGroupChat) {
                otherUserName.setText(message.UserName);
                otherUserName.setVisibility(View.VISIBLE);
            }

        }
    }



}

