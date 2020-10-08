package com.jeannypr.scientificstudy.Class.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Class.adapter.ClassListAdapter;
import com.jeannypr.scientificstudy.LearnSubject.activity.LearnListActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.StudentListActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityClassListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassListActivity extends AppCompatActivity implements View.OnClickListener {
    BaseService service;
    UserModel userData;
    private ClassListAdapter adapter;
    private Context context;
    private ArrayList<ClassModel> classes;
    private UserPreference userPreference;
    private UserModel userModel;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ProgressBar pb;
    int totalStudents;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;
    SearchView searchView;
    private ChatService mChatService;
    String schoolCode;
    ActivityClassListBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_list);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();
        schoolCode = userPreference.getSchoolCode();

        SetToolbar();
        analyticsCell = findViewById(R.id.analyticsCell);
        analyticsCell.setOnClickListener(this);

        sortCell = findViewById(R.id.sortCell);
        sortCell.setOnClickListener(this);

        searchCell = findViewById(R.id.searchCell);
        searchCell.setOnClickListener(this);

        filterCell = findViewById(R.id.filterCell);
        filterCell.setOnClickListener(this);

        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        RecyclerView list = findViewById(R.id.list);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);

        classes = new ArrayList<>();
        adapter = new ClassListAdapter(context, classes, new ClassListAdapter.OnItemClickListener() {


            @Override
            public void onLearn(ClassModel classModel) {
                startActivity(new Intent(context, LearnListActivity.class)
                        .putExtra("Id", classModel.Id)
                        .putExtra("ClassName", classModel.Name));
            }

            @Override
            public void onStudentClick(ClassModel classModel) {

//                if (/*classModel.TotalNoStudents != null && */classModel.getTotalNoStudents() > 0) {
                    Intent studentListIntent = new Intent(context, StudentListActivity.class);
                    studentListIntent.putExtra("Id", classModel.Id);
                    studentListIntent.putExtra("ClassName", classModel.Name);
                    studentListIntent.putExtra("totalStudents", classModel.getTotalNoStudents());
                    startActivity(studentListIntent);
//                }
            }

            @Override
            public void onChatIcon(ClassModel classModel) {
                if (mChatService == null)
                    mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

                final ChatRoomModel chatRoom = new ChatRoomModel();
                chatRoom.Type = Constants.ChatRoomType.GROUP;
                chatRoom.RoomName = classModel.Name;
                chatRoom.CreatedBy = userData.getUserId();
                chatRoom.setLastUserName("");
                chatRoom.setLastMessage("");
                chatRoom.OwnerName = classModel.Name;
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


        });


        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));

        service = new DataRepo<>(BaseService.class, context).getService();

        UserPreference userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        pb.setVisibility(View.VISIBLE);

        GetClassList();

    }

    private void GetClassList() {
        pb.setVisibility(View.VISIBLE);
        service.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();

                classes.clear();
                totalStudents = 0;

                if (resp.rcode == Constants.Rcode.OK) {
                    List<ClassModel> allclasss = resp.data;
                    for (ClassModel cls : allclasss) {
                        classes.add(cls);
                        totalStudents += cls.getTotalNoStudents();

                    }
                    adapter.notifyDataSetChanged();
                    SetToolbar();
                    //students = allStudents;

                } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                    noRecord.setVisibility(View.VISIBLE);
                    noRecordMsg.setText("No record found.");

                } else {
                    Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Log.d("classList", "server call error");
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "class list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetClassList();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
        if (totalStudents != 0) {
            subTitle = String.valueOf(totalStudents) + " students";
        }
        Utility.SetToolbar(context, "Classes", subTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);

        return true;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.searchCell:
                searchView.setVisibility(View.VISIBLE);
                searchView.setIconifiedByDefault(false);
                analyticsCell.setVisibility(View.GONE);
                sortCell.setVisibility(View.GONE);
                searchCell.setVisibility(View.GONE);
                filterCell.setVisibility(View.GONE);
                break;

            case R.id.searchView:
                searchView.setIconifiedByDefault(true);
                searchView.setVisibility(View.GONE);
                analyticsCell.setVisibility(View.VISIBLE);
                sortCell.setVisibility(View.VISIBLE);
                searchCell.setVisibility(View.VISIBLE);
                filterCell.setVisibility(View.VISIBLE);
                break;
        }

    }
}
