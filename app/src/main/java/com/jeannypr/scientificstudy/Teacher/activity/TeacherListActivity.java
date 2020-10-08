package com.jeannypr.scientificstudy.Teacher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.adapter.TeacherListAdapter;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityTeacherListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherListActivity extends AppCompatActivity implements View.OnClickListener {

    private TeacherListAdapter adapter;
    private Context context;
    private ProgressBar pb;
    private ArrayList<TeacherModel> teachers;
    private ActivityTeacherListBinding binding;
    private LinearLayout contentContainer, noRecord;
    private TextView noRecordMsg;
    TeacherService teacherService;
    UserPreference userPref;
    UserModel userData;
    int totalStaff;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;
    SearchView searchView;
    private ChatService mChatService;
    private String schoolCode;
    Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_list);
        context = this;

        userPref = UserPreference.getInstance(context);
        schoolCode = userPref.getSchoolCode();
        userData = userPref.getUserData();
        isAdmin = userData.getRoleTitle().equals(Constants.Role.ADMIN);

        SetToolbar();

        pb = findViewById(R.id.progressBar);
        RecyclerView teacherList = findViewById(R.id.teacher_list);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        analyticsCell = findViewById(R.id.analyticsCell);
        analyticsCell.setOnClickListener(this);

        sortCell = findViewById(R.id.sortCell);
        sortCell.setOnClickListener(this);

//        searchCell = findViewById(R.id.searchCell);
//        searchCell.setOnClickListener(this);

        filterCell = findViewById(R.id.filterCell);
        filterCell.setOnClickListener(this);

//        searchView = findViewById(R.id.searchView);
//        searchView.setOnClickListener(this);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

        teachers = new ArrayList<>();
        adapter = new TeacherListAdapter(context, teachers, new TeacherListAdapter.OnItemClickListener() {

            @Override
            public void onClick(TeacherModel teacherModel) {
                Intent teacherProfileIntent = new Intent(context, TeacherProfileActivity.class);
                teacherProfileIntent.putExtra(Constants.TEACHER_ID, teacherModel.Id);
                teacherProfileIntent.putExtra(Constants.TEACHER_USER_ID, teacherModel.UserId);
                teacherProfileIntent.putExtra("profileImage", teacherModel.ImagePath);
                teacherProfileIntent.putExtra("teacherName", teacherModel.Name);
                startActivity(teacherProfileIntent);
            }

            @Override
            public void ChatBtnClick(TeacherModel teacherModel) {

                final ChatRoomModel chatRoom = new ChatRoomModel();
                chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                chatRoom.RoomName = teacherModel.Name;
                chatRoom.CreatedBy = userData.getUserId();
                chatRoom.IsClass = false;
                chatRoom.IsAllTeacherGroup = false;
                chatRoom.setLastUserName(userData.getFirstName() + " " + userData.getLastName());
                chatRoom.setLastMessage("");
                chatRoom.OwnerName = userData.getFirstName() + " " + userData.getLastName();
                chatRoom.UserId = teacherModel.UserId;

                mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();
                mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                    @Override
                    public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                        if (call.isExecuted()) {
                            ChatStartBean data = response.body();
                            int userId = userPref.getUserData().getUserId();
                            if (data != null) {
                                if (data.Id != 0) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("chatRoomId", data.Id);
                                    intent.putExtra("chatRoomName", chatRoom.RoomName);

                                    if (!chatRoom.Type.equals(Constants.ChatRoomType.GROUP)) {
                                        intent.putExtra("otherUserId", chatRoom.CreatedBy == userId ? chatRoom.UserId : chatRoom.CreatedBy);
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
            public void showSearchMsg(Boolean isDataFound) {
                if (isDataFound) {
                    binding.teacherList.setVisibility(View.VISIBLE);
                    binding.noRecordRow.noRecord.setVisibility(View.GONE);

                } else {
                    binding.teacherList.setVisibility(View.GONE);
                    binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);

                    binding.noRecordRow.noRecordIc.setImageResource(R.drawable.ic_search);
                    binding.noRecordRow.noRecordMsg.setText(R.string.noResultFound);
                    binding.noRecordRow.noRecordMsg2.setText(R.string.noResultFoundDesc);
                }
            }
        });

        teacherList.setAdapter(adapter);
        teacherList.setLayoutManager(new LinearLayoutManager(context));

        teacherService = new DataRepo<>(TeacherService.class, context).getService();


        GetTeachers();
    }

    private void SetToolbar() {
        setSupportActionBar(binding.toolbar);
        Utility.SetToolbar(context, "Staff", totalStaff + " Staff");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTeachers();
    }

    public void GetTeachers() {
        Utility.ClearCachefromGlide(context);
        showLoader();
        teacherService.getTeachers(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {
                teachers.clear();

                totalStaff = 0;

                TeacherBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            List<TeacherModel> allTeachers = resp.data;

                            totalStaff = resp.data.size();
                            SetToolbar();

                            for (TeacherModel teacher : allTeachers) {
                                teachers.add(teacher);
                                //  adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");
                    } else {
                        Toast.makeText(context, "Teacher list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                hideLoader();

            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Log.d("teacherList", "server call error");
                hideLoader();
                Toast.makeText(context, "Teacher list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showLoader() {
        pb.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
      /*  switch (view.getId()) {
            case R.id.searchCell:
                searchView.setIconifiedByDefault(false);
                searchView.setVisibility(View.VISIBLE);
                searchCell.setVisibility(View.GONE);
                sortCell.setVisibility(View.GONE);
                filterCell.setVisibility(View.GONE);
                analyticsCell.setVisibility(View.GONE);
                break;

            case R.id.searchView:
                searchView.setIconifiedByDefault(true);
                searchView.setVisibility(View.GONE);
                searchCell.setVisibility(View.VISIBLE);
                sortCell.setVisibility(View.VISIBLE);
                filterCell.setVisibility(View.VISIBLE);
                analyticsCell.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_menu, menu);

        if (isAdmin) menu.findItem(R.id.add_staff).setVisible(true);
        else menu.findItem(R.id.add_staff).setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_staff:
                Intent i = new Intent(this, AddEditStaffActivity.class);
                startActivity(i);
                return true;

            case R.id.search_nav:
                setSearchListner(item);
                return true;

            default:
                return false;
        }
    }

    private void setSearchListner(MenuItem item) {
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

//        searchView.setQueryHint("Enter teacher name");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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

        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                return false;
            }
        });
    }
}
