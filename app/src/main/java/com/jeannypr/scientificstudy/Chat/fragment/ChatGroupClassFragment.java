package com.jeannypr.scientificstudy.Chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.activity.ChatParentListActivity;
import com.jeannypr.scientificstudy.Chat.adapter.ChatClassListAdapter;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatGroupClassFragment extends Fragment {

    UserPreference userPreference;
    String schoolCode;
    int userId;
    private Context context;
    private View view;
    private List<ClassModel> mClassList;
    RecyclerView mClassRecycler;
    ChatClassListAdapter mClassAdapter;
    private ChatService mChatService;
    ProgressBar pb;

    public ChatGroupClassFragment() {
    }

    private static final String TAG = "ChatGroupTeacherFrag";
    UserModel userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreference = UserPreference.getInstance(getActivity());
        schoolCode = userPreference.getSchoolCode();
        userData = userPreference.getUserData();
        userId = userData.getUserId();
        context = getContext();
        mClassList = new ArrayList<>();
        userPreference.SetCurrentChatRoomId(0);

        mClassAdapter = new ChatClassListAdapter(context, mClassList, new ChatClassListAdapter.OnItemClickListener() {
            @Override
            public void onClassClick(ClassModel classModel) {

                if (mChatService == null)
                    mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

                final ChatRoomModel chatRoom = new ChatRoomModel();
                chatRoom.Type = Constants.ChatRoomType.GROUP;
                chatRoom.RoomName = classModel.Name;
                chatRoom.CreatedBy = userData.getUserId();
                chatRoom.setLastUserName("");
                chatRoom.setLastMessage("");
                chatRoom.OwnerName = classModel.Name;//userData.getFirstName()+" "+userData.getLastName();
                chatRoom.UserId = 0;
                chatRoom.ClassId = classModel.Id;
                chatRoom.IsClass = true;
                chatRoom.IsAllTeacherGroup = false;

                mChatService.StartGroupChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                    @Override
                    public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                        if (call.isExecuted()) {
                            ChatStartBean data = response.body();
                            if (data != null) {

                                if (data.Id != 0) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("chatRoomId", data.Id);
                                    intent.putExtra("chatRoomName", chatRoom.RoomName);
                                    intent.putExtra("isGroup", true);
                                    intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                    intent.putExtra("IsClass", chatRoom.IsClass);
                                    intent.putExtra("ClassId", chatRoom.ClassId);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatStartBean> call, Throwable t) {
                        Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onStudentClick(ClassModel classModel) {

                if (classModel.getTotalNoStudents() > 0) {
                    Intent intent = new Intent(getActivity(), ChatParentListActivity.class);
                    intent.putExtra("Id", classModel.Id);
                    intent.putExtra("ClassName", classModel.Name);
                    startActivity(intent);
                    ;
                }

            }
        });
        context = getContext();
        setHasOptionsMenu(true);

        if (userData.getRoleTitle().equals(Constants.Role.ADMIN)) {

            BaseService service = (BaseService) new DataRepo(BaseService.class, context).getService();
            service.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean resp = response.body();
                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            for (ClassModel classModel : resp.data) {
                                mClassList.add(classModel);
                            }
                            mClassAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, resp.msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }

        if (userData.getRoleTitle().equals(Constants.Role.TEACHER)) {

            TeacherService teacherService = new DataRepo<>(TeacherService.class, context).getService();
            teacherService.GetMyClasses(userData.getSchoolId(), userData.getAcademicyearId(), userData.getUserId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean myClassesBean = response.body();
                    if (myClassesBean != null && myClassesBean.rcode == Constants.Rcode.OK) {

                        if (myClassesBean.data.size() > 0) {
                            for (ClassModel myClass : myClassesBean.data) {
                                mClassList.add(myClass);
                            }
                            mClassAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = LayoutInflater.from(context).inflate(R.layout.fragment_chat_teacher_list,
                container, false);


        mClassRecycler = view.findViewById(R.id.reyclerview_list);
        pb = view.findViewById(R.id.pbChatGroupClsFm);
        pb.setVisibility(View.VISIBLE);

        mClassRecycler.setAdapter(mClassAdapter);
        mClassRecycler.setLayoutManager(new LinearLayoutManager(context));
        pb.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search_classGroupFrag).setVisible(true);
        menu.findItem(R.id.search_staffGroupFrag).setVisible(false);
        menu.findItem(R.id.search_chatFrag).setVisible(false);

        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenu = menu.findItem(R.id.search_classGroupFrag);
        final SearchView searchView = (SearchView) searchMenu.getActionView();

        searchView.setQueryHint("Search by class name...");
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
                mClassAdapter.getFilter().filter(newText);
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