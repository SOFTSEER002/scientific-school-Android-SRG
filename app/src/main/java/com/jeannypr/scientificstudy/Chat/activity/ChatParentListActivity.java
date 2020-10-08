package com.jeannypr.scientificstudy.Chat.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.adapter.StudentListAdapterForChat;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatParentListActivity extends AppCompatActivity implements View.OnClickListener {
    private int classId;
    private String className;
    private StudentListAdapterForChat adapter;
    private Context context;
    private ProgressDialog p_dialog;
    private ArrayList<StudentModel> students;
    private UserPreference userPref;
    private String schoolCode;
    private ChatService mChatService;
    CoordinatorLayout studentListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);
        context = this;

        userPref = UserPreference.getInstance(context);
        schoolCode = userPref.getSchoolCode();

        classId = getIntent().getIntExtra("Id", -1);
        className = getIntent().getStringExtra("ClassName");

        studentListContainer = findViewById(R.id.studentListContainer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, className, "");

        if (classId == -1) {
            Toast.makeText(this, "Please select a class to view students.", Toast.LENGTH_SHORT).show();
        } else {
            RecyclerView studentList = findViewById(R.id.student_list);

            students = new ArrayList<>();
            adapter = new StudentListAdapterForChat(context, students, userPref.getUserData().getRoleTitle(), new StudentListAdapterForChat.OnItemClickListener() {
                @Override
                public void onStudentClick(final StudentModel studentModel) {

                    String msg = "You are going to enter into a private chat with the parent.\n" +
                            "Your chat can be monitored by the principal and admins.\n" +
                            "Are you sure to proceed? ";
                    final View btnView = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, studentListContainer, false);

                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage(msg)
                            .setView(btnView)
                            .show();

                    Button positiveBtn = btnView.findViewById(R.id.positiveBtn);
                    Button negativeBtn = btnView.findViewById(R.id.negativeBtn);
                    negativeBtn.setVisibility(View.VISIBLE);

                    positiveBtn.setText(getResources().getString(R.string.dialogPositiveButtonOk));
                    negativeBtn.setText(getResources().getString(R.string.dialogNegativeButtonCancel));

                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chatRoomOwnerName = userPref.getUserData().getFirstName() + " " + userPref.getUserData().getLastName();
                            int teacherUserId = userPref.getUserData().getUserId();
                            String chatRoomName = studentModel.Name + "-" + studentModel.AdmissionNo;

                            final ChatRoomModel chatRoom = new ChatRoomModel();
                            chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                            chatRoom.RoomName = chatRoomName;
                            chatRoom.CreatedBy = teacherUserId;
                            chatRoom.IsClass = false;
                            chatRoom.IsAllTeacherGroup = false;
                            chatRoom.setLastUserName("");
                            chatRoom.setLastMessage("");
                            chatRoom.OwnerName = chatRoomOwnerName;//userData.getFirstName()+" "+userData.getLastName();
                            chatRoom.UserId = studentModel.ParentUserId;

                            if (mChatService == null)
                                mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

                            mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                                @Override
                                public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                                    if (call.isExecuted()) {
                                        ChatStartBean data = response.body();
                                        int userId = userPref.getUserData().getUserId();

                                        if (data.Id != 0) {
                                            Intent intent = new Intent(context, ChatActivity.class);
                                            intent.putExtra("chatRoomId", data.Id);
                                            intent.putExtra("chatRoomName", chatRoom.RoomName);

                                            if (!chatRoom.Type.equals(Constants.ChatRoomType.GROUP)) {
                                                intent.putExtra("otherUserId", chatRoom.CreatedBy == userId ? chatRoom.UserId : chatRoom.CreatedBy);
                                            } else {
                                                if (chatRoom.IsClass) {
                                                    intent.putExtra("IsClass", chatRoom.IsClass);
                                                    intent.putExtra("ClassId", chatRoom.ClassId);
                                                } else if (chatRoom.IsAllTeacherGroup) {
                                                    intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                                }
                                            }


                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                        }
                                    } else {
                                        Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ChatStartBean> call, Throwable t) {
                                    Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                }
                            });

                            dialog.dismiss();
                        }
                    });

                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onChatBtnClick(StudentModel studentModel) {
                }
            });

            studentList.setAdapter(adapter);

           /* studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView,
                                        View view, int position, long rowId) {
                    final StudentModel studentModel = adapter.getItem(position);

                   *//* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    String msg = "You are going to enter into a private chat with the parent.\n" +
                            "Your chat can be monitored by the principal and admins.\n" +
                            "Are you sure to proceed? ";

                    builder.setMessage(msg)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    String chatRoomOwnerName = userPref.getUserData().FirstName + " " + userPref.getUserData().LastName;
                                    int teacherUserId = userPref.getUserData().UserId;
                                    String chatRoomName = studentModel.Name + "-" + studentModel.AdmissionNo;

                                    final ChatRoomModel chatRoom = new ChatRoomModel();
                                    chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                                    chatRoom.RoomName = chatRoomName;
                                    chatRoom.CreatedBy = teacherUserId;
                                    chatRoom.IsClass = false;
                                    chatRoom.IsAllTeacherGroup = false;
                                    chatRoom.setLastUserName("");
                                    chatRoom.setLastMessage("");
                                    chatRoom.OwnerName = chatRoomOwnerName;//userData.getFirstName()+" "+userData.getLastName();
                                    chatRoom.UserId = studentModel.ParentUserId;


                                    if (mChatService == null)
                                        mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();


                                    mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                                        @Override
                                        public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                                            if (call.isExecuted()) {
                                                ChatStartBean data = response.body();
                                                int userId = userPref.getUserData().UserId;

                                                if (data.Id != 0) {
                                                    Intent intent = new Intent(context, ChatActivity.class);
                                                    intent.putExtra("chatRoomId", data.Id);
                                                    intent.putExtra("chatRoomName", chatRoom.RoomName);

                                                    if (!chatRoom.Type.equals(Constants.ChatRoomType.GROUP)) {
                                                        intent.putExtra("otherUserId", chatRoom.CreatedBy == userId ? chatRoom.UserId : chatRoom.CreatedBy);
                                                    } else {
                                                        if (chatRoom.IsClass) {
                                                            intent.putExtra("IsClass", chatRoom.IsClass);
                                                            intent.putExtra("ClassId", chatRoom.ClassId);
                                                        } else if (chatRoom.IsAllTeacherGroup) {
                                                            intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                                        }
                                                    }


                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                                }
                                            } else {
                                                Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ChatStartBean> call, Throwable t) {
                                            Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                        }
                                    });
                                }
                            })

                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // User cancelled the dialog
                                }

                            });
                    // Create the AlertDialog object and return it
                    AlertDialog dialog = builder.create();
                    dialog.show();*//*

             *//*  String msg = "You are going to enter into a private chat with the parent.\n" +
                            "Your chat can be monitored by the principal and admins.\n" +
                            "Are you sure to proceed? ";
                    final View btnView = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, studentListContainer, false);

                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage(msg)
                            .setView(btnView)
                            .show();

                    Button positiveBtn = btnView.findViewById(R.id.positiveBtn);
                    Button negativeBtn = btnView.findViewById(R.id.negativeBtn);
                    negativeBtn.setVisibility(View.VISIBLE);

                    positiveBtn.setText(getResources().getString(R.string.dialogPositiveButtonOk));
                    negativeBtn.setText(getResources().getString(R.string.dialogNegativeButtonCancel));

                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String chatRoomOwnerName = userPref.getUserData().FirstName + " " + userPref.getUserData().LastName;
                            int teacherUserId = userPref.getUserData().UserId;
                            String chatRoomName = studentModel.Name + "-" + studentModel.AdmissionNo;

                            final ChatRoomModel chatRoom = new ChatRoomModel();
                            chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
                            chatRoom.RoomName = chatRoomName;
                            chatRoom.CreatedBy = teacherUserId;
                            chatRoom.IsClass = false;
                            chatRoom.IsAllTeacherGroup = false;
                            chatRoom.setLastUserName("");
                            chatRoom.setLastMessage("");
                            chatRoom.OwnerName = chatRoomOwnerName;//userData.getFirstName()+" "+userData.getLastName();
                            chatRoom.UserId = studentModel.ParentUserId;

                            if (mChatService == null)
                                mChatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();

                            mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
                                @Override
                                public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                                    if (call.isExecuted()) {
                                        ChatStartBean data = response.body();
                                        int userId = userPref.getUserData().UserId;

                                        if (data.Id != 0) {
                                            Intent intent = new Intent(context, ChatActivity.class);
                                            intent.putExtra("chatRoomId", data.Id);
                                            intent.putExtra("chatRoomName", chatRoom.RoomName);

                                            if (!chatRoom.Type.equals(Constants.ChatRoomType.GROUP)) {
                                                intent.putExtra("otherUserId", chatRoom.CreatedBy == userId ? chatRoom.UserId : chatRoom.CreatedBy);
                                            } else {
                                                if (chatRoom.IsClass) {
                                                    intent.putExtra("IsClass", chatRoom.IsClass);
                                                    intent.putExtra("ClassId", chatRoom.ClassId);
                                                } else if (chatRoom.IsAllTeacherGroup) {
                                                    intent.putExtra("IsAllTeacherGroup", chatRoom.IsAllTeacherGroup);
                                                }
                                            }


                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                        }
                                    } else {
                                        Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ChatStartBean> call, Throwable t) {
                                    Toast.makeText(context, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG);
                                }
                            });

                            dialog.dismiss();
                        }
                    });

                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });*//*
                }
            });*/

            StudentService studentService = new DataRepo<>(StudentService.class, context).getService();

            showLoader();
            studentService.getStudents(classId).enqueue(new Callback<StudentBean>() {
                @Override
                public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {
                    StudentBean resp = response.body();
                    if (resp != null) {

                        if (resp.rcode == Constants.Rcode.OK) {
                            List<StudentModel> allStudents = resp.data;
                            for (StudentModel student : allStudents) {
                                students.add(student);
                                adapter.notifyDataSetChanged();
                            }
                            //students = allStudents;

                        } else {
                            Toast.makeText(context, "Student list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    hideLoader();

                }

                @Override
                public void onFailure(Call<StudentBean> call, Throwable t) {
                    Log.d("studentList", "server call error");
                    hideLoader();
                    Toast.makeText(context, "Student list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void showLoader() {
        p_dialog = Utility.showProgressDialog(context, "Getting student list. Please wait...");

    }

    private void hideLoader() {
        p_dialog.dismiss();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnBack:
//                this.finish();
//                break;
        }

    }
}