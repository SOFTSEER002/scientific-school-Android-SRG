package com.jeannypr.scientificstudy.Chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.adapter.OpenChatRoomListAdapter;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomBean;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    UserPreference userPreference;
    String schoolCode;
    int userId;
    private Context context;
    private View view;
    private LayoutInflater inflater;
    // private LinearLayout chat_group_list;
    private RecyclerView chat_group_list;
    final String ALL_TEACHERS = "ALL TEACHERS";
    String ClassIds;
    ProgressBar pb;
    OpenChatRoomListAdapter adapter;
    List<ChatRoomModel> chatRooms;
    String roleTitle;
    TextView noRecordMsg;
    LinearLayout noRecord;

    public ChatFragment() {
    }

    UserModel userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreference = UserPreference.getInstance(getActivity());
        schoolCode = userPreference.getSchoolCode();
        userData = userPreference.getUserData();
        userId = userData.getUserId();
        roleTitle = userData.getRoleTitle();
        userPreference.SetCurrentChatRoomId(0);

        if (roleTitle.equals(Constants.Role.PARENT)) {
            children = userPreference.getChildren();
        }

        context = getActivity();
        inflater = LayoutInflater.from(context);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat_list,
                container, false);

        chat_group_list = view.findViewById(R.id.chat_group_list);
        chatRooms = new ArrayList<>();
        adapter = new OpenChatRoomListAdapter(context, chatRooms, children, new OpenChatRoomListAdapter.OnItemClickListner() {

            @Override
            public void OnRoomClick(ChatRoomModel model) {
                try {

                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatRoomId", model.Id);
                    intent.putExtra("chatRoomName", model.RoomName);
                    intent.putExtra("isGroup", model.Type.equals(Constants.ChatRoomType.GROUP));

                    if (!model.Type.equals(Constants.ChatRoomType.GROUP)) {
                        intent.putExtra("otherUserId", model.CreatedBy == userId ? model.UserId : model.CreatedBy);
                    } else {
                        if (model.IsClass) {
                            intent.putExtra("IsClass", true);
                            intent.putExtra("ClassId", model.ClassId);
                        } else if (model.IsAllTeacherGroup) {
                            intent.putExtra("IsAllTeacherGroup", true);
                        }
                    }
                    startActivity(intent);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        chat_group_list.setAdapter(adapter);
        chat_group_list.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    List<ChildModel> children;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pb = view.findViewById(R.id.pbChatFm);
        noRecordMsg = view.findViewById(R.id.noRecordMsg);
        noRecord = view.findViewById(R.id.noRecord);

        ShowChatRooms();
    }

    private void ShowChatRooms() {
        pb.setVisibility(View.VISIBLE);

        switch (roleTitle) {
            case Constants.Role.ADMIN:
                loadChatRooms(userId, roleTitle, null);
                break;

            case Constants.Role.TEACHER:

                TeacherService teacherService = new DataRepo<>(TeacherService.class, context).getService();
                teacherService.GetMyClasses(userData.getSchoolId(), userData.getAcademicyearId(), userData.getUserId()).enqueue(new Callback<ClassBean>() {
                    @Override
                    public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                        ClassBean myClassesBean = response.body();
                        if (myClassesBean != null && myClassesBean.rcode == Constants.Rcode.OK) {

                            if (myClassesBean.data.size() > 0) {
                                ArrayList<String> classes = new ArrayList<>();
                                for (ClassModel myClass : myClassesBean.data) {
                                    classes.add(myClass.Id.toString());
                                }
                                ClassIds = Utility.Join(",", classes);
                            }
                        }
                        loadChatRooms(userId, roleTitle, ClassIds);
                    }

                    @Override
                    public void onFailure(Call<ClassBean> call, Throwable t) {
                        loadChatRooms(userId, roleTitle, ClassIds);
                        // Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case Constants.Role.PARENT:


                if (children != null) {
                    ArrayList<String> classes = new ArrayList<>();

                    for (ChildModel child : children) {
                        if (child.ClassId != 0) {
                            classes.add(child.ClassId.toString());
                        }
                    }
                    ClassIds = Utility.Join(",", classes);

                    loadChatRooms(userId, roleTitle, ClassIds);
                }
                break;
        }
    }

    private void loadChatRooms(int userId, String roleTitle, String classIds) {

        ChatService service = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();
        service.GetUserOpenChatRooms(userId, roleTitle, classIds, schoolCode).enqueue(new Callback<ChatRoomBean>() {

            @Override
            public void onResponse(Call<ChatRoomBean> call, Response<ChatRoomBean> response) {

                chatRooms.clear();

                ChatRoomBean data = response.body();
                if (data != null && data.ChatRooms != null && data.ChatRooms.size() > 0) {
                    // updateUI(data.ChatRooms);
                    for (ChatRoomModel chatRoom : data.ChatRooms) {
                        if (!chatRoom.getLastMessage().equals("")) {
                            chatRooms.add(chatRoom);
                        }
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(context, "No chats", Toast.LENGTH_SHORT).show();
                    noRecord.setVisibility(View.VISIBLE);
                    noRecordMsg.setText("No chat found");
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ChatRoomBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Unable to get chats!", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void updateUI(List<ChatRoomModel> chatRooms) {

        for (final ChatRoomModel chatRoom : chatRooms) {
            //  final String ChatRoomName;
            if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {

                if (chatRoom.Type.equals(Constants.ChatRoomType.INDIVIDUAL)) {
                    chatRoom.RoomName = (chatRoom.CreatedBy != userId ? chatRoom.OwnerName : chatRoom.RoomName);
                } else {
                    for (ChildModel child : children) {
                        if (child.ClassId == chatRoom.ClassId) {
                            chatRoom.RoomName = chatRoom.RoomName + "-" + child.Name;
                            break;
                        }
                    }
                }
            } else {
                chatRoom.RoomName = chatRoom.Type.equals(Constants.ChatRoomType.INDIVIDUAL) ?
                        (chatRoom.CreatedBy != userId ? chatRoom.OwnerName : chatRoom.RoomName) : chatRoom.RoomName;
            }


           /* if( mChatRooms.contains(chatRoomId)){

                ConstraintLayout group =  chat_group_list.findViewWithTag(chatRoomId);
                // TextView chat_group_name = view.findViewById(R.id.chat_group_name);
                TextView chat_last_message  = group.findViewById(R.id.chat_last_message);
                TextView chat_last_date = group.findViewById(R.id.chat_last_date);
                // Button chat_group_unread_message = view.findViewById(R.id.chat_group_unread_message);
                chat_last_message.setText(chatRoom.LastMessage);
                chat_last_date.setText(Utility.GetChatTime(chatRoom.LastMessageTimeStamp));
                return;
            }*/

//            mChatRooms.add(chatRoomId);
//
            final ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.row_chat_users, chat_group_list, false);
            TextView chat_group_name = view.findViewById(R.id.chat_group_name);
            TextView chat_last_message = view.findViewById(R.id.chat_last_message);
            TextView chat_last_date = view.findViewById(R.id.chat_last_date);
            Button chat_group_unread_message = view.findViewById(R.id.chat_group_unread_message);
            chat_group_name.setText(chatRoom.RoomName != null ? chatRoom.RoomName : "");
            chat_last_message.setText(chatRoom.getLastUserName().concat(": ").concat(chatRoom.getLastMessage()));

            //TODO://set last timestamp
            chat_last_date.setText(Utility.GetChatTime(chatRoom.LastMessageTimeStamp));
            //chat_group_unread_message.setText();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("chatRoomId", chatRoom.Id);
                    intent.putExtra("chatRoomName", chatRoom.RoomName);
                    intent.putExtra("isGroup", chatRoom.Type.equals(Constants.ChatRoomType.GROUP));
                    startActivity(intent);

                }
            });

            view.setTag(chatRoom.Id);
            chat_group_list.addView(view);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ShowChatRooms();
    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }*/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        /*TabLayout tabhost =(TabLayout) getActivity().findViewById(R.id.viewpager);
        int selectedTabPosition = tabhost.getSelectedTabPosition();*/

        ViewPager tabhost = (ViewPager) getActivity().findViewById(R.id.viewpager);
        int selectedTabPosition = tabhost.getCurrentItem();

        switch (selectedTabPosition) {
            case 0:
                menu.findItem(R.id.search_classGroupFrag).setVisible(false);
                menu.findItem(R.id.search_staffGroupFrag).setVisible(false);
                menu.findItem(R.id.search_chatFrag).setVisible(true);
                break;

            case 1:
                menu.findItem(R.id.search_classGroupFrag).setVisible(false);
                menu.findItem(R.id.search_staffGroupFrag).setVisible(true);
                menu.findItem(R.id.search_chatFrag).setVisible(false);
                break;
        }

        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenu = menu.findItem(R.id.search_chatFrag);
        final SearchView searchView = (SearchView) searchMenu.getActionView();
        /*  searchView.setBackgroundColor(Color.WHITE);*/

        searchView.setQueryHint("Search...");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                return false;
            }
        });
    }
}

