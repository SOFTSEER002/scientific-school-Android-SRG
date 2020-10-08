package com.jeannypr.scientificstudy.Attendance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.StudentWiseAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.MonthwiseAttendanceSummaryActivity;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.MonthwiseAttendanceBean;
import com.jeannypr.scientificstudy.Student.model.MonthwiseAttendanceModel;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentWiseAttenenceActivity extends AppCompatActivity {
    int classId;
    String className;
    public Context context;
    StudentService studentService;
    private Spinner studentList;
    private DropDownAdapter studentListAdapter;
    DropDownModel selectedStudent;
    StudentAttendanceModel studentAttendanceModel;
    ArrayList<DropDownModel> students;
    ArrayList<MonthwiseAttendanceModel> attendance;
    int studentId;
    String studentname;
    UserPreference userPreference;
    UserModel userModel;
    RecyclerView attendanceList;
    StudentWiseAttendanceAdapter attendanceAdapter;
    ProgressBar pb;
    TextView noRecordMsg;
    LinearLayout noRecord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_wise_attendance);
        context = this;

        classId = getIntent().getIntExtra("classId", -1);
        className = getIntent().getStringExtra("className");

        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();

        studentService = new DataRepo<>(StudentService.class, context).getService();
        students = new ArrayList<>();
        studentListAdapter = new DropDownAdapter(StudentWiseAttenenceActivity.this, R.layout.row_spinner, students);

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Student Wise Report", className);

        attendance = new ArrayList<>();
        attendanceAdapter = new StudentWiseAttendanceAdapter(context, attendance, new StudentWiseAttendanceAdapter.OnItemClickListener() {

            @Override
            public void onClick(MonthwiseAttendanceModel model) {

                Intent intent = new Intent(context, MonthwiseAttendanceSummaryActivity.class);

                intent.putExtra("classId", classId);
                intent.putExtra("studentId", studentId);
                intent.putExtra("schoolId", userModel.getSchoolId());
                intent.putExtra("academicyearId", userModel.getAcademicyearId());
                intent.putExtra("monthId", model.MonthId);
                intent.putExtra("studentName", studentname);
                intent.putExtra("className", className);
                startActivity(intent);
            }
        });

        pb = findViewById(R.id.progressBar);
        studentList = findViewById(R.id.ddlStudentList);
        studentList.setAdapter(studentListAdapter);

        attendanceList = findViewById(R.id.attendanceList);
        attendanceList.setAdapter(attendanceAdapter);

        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select student");
        defaultOption.setId(-1);
        students.add(defaultOption);


        studentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedStudent = studentListAdapter.getItem(position);
                if (selectedStudent != null) {
                    if (selectedStudent.getId() == -1) {
                        // selectedStudent = null;
                        studentId = 0;
                        studentname = "";
                    } else {
                        studentId = selectedStudent.getId();
                        studentname = selectedStudent.getText();

                        GetStudentAttendance();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetStudentList();

    }

    private void GetStudentAttendance() {
        pb.setVisibility(View.VISIBLE);
        studentService.GetStudentWiseAttendanceReport(classId, studentId, userModel.getSchoolId(), userModel.getAcademicyearId())
                .enqueue(new Callback<MonthwiseAttendanceBean>() {
                    @Override
                    public void onResponse(Call<MonthwiseAttendanceBean> call, Response<MonthwiseAttendanceBean> response) {
                        MonthwiseAttendanceBean resp = response.body();
                        pb.setVisibility(View.GONE);

                        if (resp != null) {

                            if (resp.rcode == Constants.Rcode.OK) {
                                attendance.clear();
                                if (resp.data != null) {
                                    for (MonthwiseAttendanceModel datum : resp.data) {
                                        attendance.add(datum);
                                    }
                                    attendanceAdapter.notifyDataSetChanged();
                                }
                            } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                                Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record Found");
                            }
                        } else {
                            Toast.makeText(context, "Attendance report could not be loaded .please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MonthwiseAttendanceBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void GetStudentList() {
        pb.setVisibility(View.VISIBLE);
        studentService.getStudents(classId).enqueue(new Callback<StudentBean>() {
            @Override
            public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {

                StudentBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            List<StudentModel> studentList = resp.data;
                            for (StudentModel studentModel : studentList) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(studentModel.Id);
                                dropDownModel.setText(studentModel.Name);
                                students.add(dropDownModel);
                            }
                            studentListAdapter.notifyDataSetChanged();
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Student list could not be loaded .Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StudentBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
