package com.jeannypr.scientificstudy.Student.activity;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.activity.BaseActivity;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.adapter.StudentAdapter;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityStudentlistBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends AppCompatActivity implements View.OnClickListener {

    private int classId;
    private String className;
    private StudentAdapter adapter;
    private Context context;
    private ProgressBar pb;
    private ArrayList<StudentModel> students;
//    private LinearLayout noRecord;
//    private TextView noRecordMsg;
    int totalStudents;
    UserModel userModel;
    ActivityStudentlistBinding binding;
    ConstraintLayout analyticsCell, sortCell, searchCell, filterCell;
    SearchView searchView;
    private String schoolCode;
    private ChatService mChatService;
    Boolean canSeeContactNo = false;
    UserPreference userPref;
    Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_studentlist);
        context = this;

        totalStudents = 0;
        classId = getIntent().getIntExtra("Id", -1);
        className = getIntent().getStringExtra("ClassName");
        if (getIntent().hasExtra("totalStudents")) {
            totalStudents = getIntent().getIntExtra("totalStudents", 0);
        }

        userPref = UserPreference.getInstance(context);
        schoolCode = userPref.getSchoolCode();
        userModel = userPref.getUserData();
        isAdmin = userModel.getRoleTitle().equals(Constants.Role.ADMIN);

        SetToolbar();

        pb = findViewById(R.id.progressBar);
//        noRecord = findViewById(R.id.noRecord);
//        noRecordMsg = findViewById(R.id.noRecordMsg);

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


        GetStudentList();
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = "";
        if (totalStudents != 0) {
            subTitle = totalStudents + " students";
        }
        Utility.SetToolbar(context, className, subTitle);
    }

    @Override
    protected void onResume() {
        GetStudentList();
        super.onResume();
    }

    private void GetStudentList() {
        Utility.ClearCachefromGlide(context);
        final SchoolDetailModel detail;

        if (classId == -1)
            Toast.makeText(this, "Please select a class to view students.", Toast.LENGTH_SHORT).show();
         else {
            RecyclerView studentList = findViewById(R.id.student_list);

            students = new ArrayList<>();
            String role = userModel.getRoleTitle();

            adapter = new StudentAdapter(context, students, role, new StudentAdapter.OnItemClickListener() {
                @Override
                public void onStudentClick(StudentModel studentModel) {

                    Intent studentProfileIntent = new Intent(context, StudentProfileActivity.class);
                    if (studentModel != null) {
                        studentProfileIntent.putExtra("studentId", studentModel.Id);
                        studentProfileIntent.putExtra("classId", classId);
                        studentProfileIntent.putExtra("studentName", studentModel.Name);
                        studentProfileIntent.putExtra("className", className);
                        studentProfileIntent.putExtra("imgPath", studentModel.ImagePath);
                        studentProfileIntent.putExtra("admNo", studentModel.AdmissionNo);
                        studentProfileIntent.putExtra("parentUserId", studentModel.ParentUserId);
                    }
                    startActivity(studentProfileIntent);
                }

                @Override
                public void onChatBtnClick(StudentModel studentModel) {

                    String chatRoomOwnerName = userModel.getFirstName() + " " + userModel.getLastName();
                    int teacherUserId = userModel.getUserId();
                    String chatRoomName = studentModel.Name + "-" + studentModel.AdmissionNo;

                    final ChatRoomModel chatRoom = new ChatRoomModel();
                    chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                    chatRoom.RoomName = chatRoomName;
                    chatRoom.CreatedBy = teacherUserId;
                    chatRoom.IsClass = false;
                    chatRoom.IsAllTeacherGroup = false;
                    chatRoom.setLastUserName("");
                    chatRoom.setLastMessage("");
                    chatRoom.OwnerName = chatRoomOwnerName;
                    chatRoom.UserId = studentModel.ParentUserId;

                    enableChat(chatRoom, schoolCode, context);
                }

                @Override
                public void showSearchMsg(Boolean isDataFound) {
                    if (isDataFound) {
                        binding.studentList.setVisibility(View.VISIBLE);
                        binding.noRecordRow.noRecord.setVisibility(View.GONE);

                    } else {
                        binding.studentList.setVisibility(View.GONE);
                        binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);

                        binding.noRecordRow.noRecordIc.setImageResource(R.drawable.ic_search);
                        binding.noRecordRow.noRecordMsg.setText(R.string.noResultFound);
                        binding.noRecordRow.noRecordMsg2.setText(R.string.noResultFoundDesc);
                    }
                }
            });

            studentList.setAdapter(adapter);
            StudentService studentService = new DataRepo<>(StudentService.class, context).getService();

            pb.setVisibility(View.VISIBLE);
            studentService.getStudents(classId).enqueue(new Callback<StudentBean>() {
                @Override
                public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {
                    students.clear();

                    StudentBean resp = response.body();
                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {

                            if (resp.data != null) {
                                List<StudentModel> allStudents = resp.data;
                                totalStudents = allStudents.size();
                                SetToolbar();

                                for (StudentModel student : allStudents) {
                                    students.add(student);
                                }
                                adapter.notifyDataSetChanged();

                                if (!userModel.getRoleTitle().equals(Constants.Role.ADMIN))
                                    userPref.getSchoolData().setCanSeeContactNumber(resp.data.get(0).getCanSeeContactNumber());
                            }

                        } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                            binding.noRecordRow.noRecord.setVisibility(View.VISIBLE);
                            binding.noRecordRow.noRecordMsg.setText(getResources().getString(R.string.noStudentMsg));
                        } else {
                            Toast.makeText(context, "Student list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<StudentBean> call, Throwable t) {
                    Log.d("Student List", "server call error");
                    pb.setVisibility(View.GONE);
                    Toast.makeText(context, "Student list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        if (isAdmin) menu.findItem(R.id.add_student).setVisible(true);
        else menu.findItem(R.id.add_student).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.add_student:
                Intent i = new Intent(this, AddEditStudentActivity.class);
                i.putExtra(Constants.CLASS_ID, classId);
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

    private void enableChat(ChatRoomModel chatRoom, String schoolCode, Context mContext) {
        startIndividualChatRoom(chatRoom, schoolCode, mContext);
    }

    private void startIndividualChatRoom(final ChatRoomModel chatRoom, String schoolCode, final Context mContext) {
        ChatService mChatService = new DataRepo<>(ChatService.class, mContext, ApiConstants.CHAT_BASE_URL).getService();
        mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
            @Override
            public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                if (call.isExecuted()) {
                    ChatStartBean data = response.body();
                    int userId = UserPreference.getInstance(mContext).getUserData().getUserId();

                    if (data != null) {
                        if (data.Id != 0) {

                            Intent intent = new Intent(mContext, ChatActivity.class);
                            intent.putExtra("chatRoomId", data.Id);
                            intent.putExtra("chatRoomName", chatRoom.RoomName);

                            if (chatRoom.Type != Constants.ChatRoomType.GROUP) {
                                int otherUserId = 0;
                                if (chatRoom.CreatedBy == userId) otherUserId = chatRoom.UserId;
                                else otherUserId = chatRoom.CreatedBy;

                                intent.putExtra("otherUserId", otherUserId);
                            }
                            startActivity(intent);

                        } else
                            Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChatStartBean> call, Throwable t) {
                Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
