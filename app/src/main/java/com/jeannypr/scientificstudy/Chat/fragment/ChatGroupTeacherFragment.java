package com.jeannypr.scientificstudy.Chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.adapter.ChatTeacherListAdapter;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.SubjectTeacherBean;
import com.jeannypr.scientificstudy.Student.model.SubjectTeacherModel;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatGroupTeacherFragment extends Fragment {

    UserPreference userPreference;
    String schoolCode;
    int userId;
    private Context context;
    private View view;
    private List<TeacherModel> mTeacherList;
    private LayoutInflater inflater;
    final String ALL_TEACHERS = "ALL TEACHERS";
    RecyclerView mTeacherRecycler;
    ChatTeacherListAdapter mTeacherAdapter;
    ChatService mChatService;

    public ChatGroupTeacherFragment() {

    }

    private static final String TAG = "ChatGroupTeacherFrag";
    UserModel userData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        setHasOptionsMenu(true);

        userPreference = UserPreference.getInstance(getActivity());
        schoolCode = userPreference.getSchoolCode();
        userData = userPreference.getUserData();
        userPreference.SetCurrentChatRoomId(0);

        userId = userData.getUserId();
        mTeacherList = new ArrayList<>();

        mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

        mTeacherAdapter = new ChatTeacherListAdapter(context, mTeacherList, new ChatTeacherListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final TeacherModel teacherModel) {
                if (teacherModel.Id == -1 && teacherModel.Name.equals(ALL_TEACHERS)) {
                    String name = userData.getFirstName() != null ? userData.getFirstName() : "" + " " + userData.getLastName() != null ? userData.getLastName() : "";

                    final ChatRoomModel chatRoom = new ChatRoomModel();
                    chatRoom.Type = Constants.ChatRoomType.GROUP;
                    chatRoom.RoomName = ALL_TEACHERS;
                    chatRoom.CreatedBy = userData.getUserId();
                    chatRoom.IsClass = false;
                    chatRoom.IsAllTeacherGroup = true;

                    chatRoom.setLastUserName("");
                    chatRoom.setLastMessage("");
                    chatRoom.OwnerName = name;
                    chatRoom.UserId = 0;

                    mChatService.StartGroupChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                        @Override
                        public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                            if (call.isExecuted()) {
                                ChatStartBean data = response.body();

                                if (data != null && data.Id != 0) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("chatRoomId", data.Id);
                                    intent.putExtra("chatRoomName", chatRoom.RoomName);
                                    intent.putExtra("isGroup", true);
                                    intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                    intent.putExtra("IsClass", false);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
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


                } else {
                    if (teacherModel.UserId != 0 && teacherModel.Name != null && !teacherModel.Name.equals("")) {


                        if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {

                            //parent-subject/class teacher chat
                            //parent is initating the chat with the teacher.

                            ChildModel child = userPreference.getSelectedChild();
                            String chatRoomName = child.Name + "-" + child.AdmissionNumber;

                            final ChatRoomModel chatRoom = new ChatRoomModel();
                            chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                            chatRoom.RoomName = teacherModel.Name;
                            chatRoom.CreatedBy = userId;
                            chatRoom.IsClass = false;
                            chatRoom.IsAllTeacherGroup = false;
                            chatRoom.setLastUserName(userData.getFirstName() + " " + userData.getLastName());
                            chatRoom.setLastMessage("");
                            chatRoom.OwnerName = chatRoomName;//userData.getFirstName()+" "+userData.getLastName();
                            chatRoom.UserId = teacherModel.UserId;

                            createIndividualChatRoom(chatRoom);

                        } else {

                            //teacher-teacher chat
                            ChatRoomModel chatRoom = new ChatRoomModel();
                            chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                            chatRoom.RoomName = teacherModel.Name;
                            chatRoom.CreatedBy = userId;
                            chatRoom.IsClass = false;
                            chatRoom.IsAllTeacherGroup = false;
                            chatRoom.setLastUserName(userData.getFirstName() + " " + userData.getLastName());
                            chatRoom.setLastMessage("");
                            chatRoom.OwnerName = userData.getFirstName() + " " + userData.getLastName();
                            chatRoom.UserId = teacherModel.UserId;

                            createIndividualChatRoom(chatRoom);

                        }

                    } else {
                        Toast.makeText(context, "Could not initiate chat with the teacher.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        if (!userData.getRoleTitle().equals(Constants.Role.PARENT)) {
            TeacherService service = (TeacherService) new DataRepo(TeacherService.class, context).getService();
            service.getTeachers(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
                @Override
                public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {
                    TeacherBean resp = response.body();
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data.size() > 1) {
                            TeacherModel teacherModel = new TeacherModel();
                            teacherModel.Name = ALL_TEACHERS;
                            teacherModel.Email = "Click to start a group chat";
                            teacherModel.Id = -1;
                            mTeacherList.add(teacherModel);
                        }
                        for (TeacherModel teacherModel : resp.data) {
                            mTeacherList.add(teacherModel);
                        }
                        mTeacherAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, resp.msg);
                    }
                }

                @Override
                public void onFailure(Call<TeacherBean> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        } else {
            int classId = userPreference.getSelectedChild().ClassId;
            StudentService studentService = new DataRepo<>(StudentService.class, context).getService();
            studentService.getSubjectTeachers(classId, userData.getAcademicyearId()).enqueue(new Callback<SubjectTeacherBean>() {
                @Override
                public void onResponse(Call<SubjectTeacherBean> call, Response<SubjectTeacherBean> response) {
                    SubjectTeacherBean resp = response.body();
                    if (resp != null) {

                        if (resp.rcode == Constants.Rcode.OK) {

                            for (SubjectTeacherModel teacher : resp.SubjectTeachers) {
                                TeacherModel teacherModel = new TeacherModel();
                                teacherModel.Name = teacher.TeacherName;
                                teacherModel.Email = teacher.Subjects;
                                teacherModel.UserId = teacher.TeacherUserId;
                                mTeacherList.add(teacherModel);
                            }
                            mTeacherAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "Teachers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Teachers list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SubjectTeacherBean> call, Throwable t) {
                    Toast.makeText(context, "Could not load teachers list!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void createIndividualChatRoom(final ChatRoomModel chatRoom) {
        mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
            @Override
            public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                if (call.isExecuted()) {
                    ChatStartBean data = response.body();
                    if (data != null) {
                        if (data.Id != 0) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("chatRoomId", data.Id);
                            intent.putExtra("chatRoomName", chatRoom.RoomName);

                            if (!chatRoom.Type.equals(Constants.ChatRoomType.GROUP)) {
                                intent.putExtra("otherUserId", chatRoom.CreatedBy == userId ? chatRoom.UserId : chatRoom.CreatedBy);
                            } else {
                                if (chatRoom.IsClass) {
                                    intent.putExtra("IsClass", chatRoom.IsClass);
                                } else if (chatRoom.IsAllTeacherGroup) {
                                    intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                }
                            }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = LayoutInflater.from(context).inflate(R.layout.fragment_chat_teacher_list,
                container, false);


        mTeacherRecycler = view.findViewById(R.id.reyclerview_list);

        mTeacherRecycler.setAdapter(mTeacherAdapter);
        mTeacherRecycler.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    /*    menu.findItem(R.id.search_classGroupFrag).setVisible(false);
        menu.findItem(R.id.search_staffGroupFrag).setVisible(true);
        menu.findItem(R.id.search_chatFrag).setVisible(false);*/

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

        MenuItem searchMenu = menu.findItem(R.id.search_staffGroupFrag);
        final SearchView searchView = (SearchView) searchMenu.getActionView();

        searchView.setQueryHint("Search by teacher name...");
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
                mTeacherAdapter.getFilter().filter(newText);
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