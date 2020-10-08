package com.jeannypr.scientificstudy.Exam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.EnterRemarksAdapter;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkBean;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkJsonModel;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkModel;
import com.jeannypr.scientificstudy.Exam.model.StudentRemarkSaveModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterRemarksActivity extends AppCompatActivity {

    private int classId;
    private String className;
    private int testId;
    private String testName;
    private ExamService service;
    private Context context;
    UserModel userdata;
    LayoutInflater inflater;
    RecyclerView studentList;
    List<StudentRemarkModel> data;
    FloatingActionButton btnSave;
    ProgressBar pb;
    ScrollView sc;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    EnterRemarksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_enter_remarks);

        Intent intent = getIntent();
        userdata = UserPreference.getInstance(context).getUserData();
        classId = intent.getIntExtra("classId", -1);
        testId = intent.getIntExtra("testId", -1);

        inflater = LayoutInflater.from(context);//.inflate()
        service = new DataRepo<>(ExamService.class, context).getService();

        data = new ArrayList<>();
        adapter = new EnterRemarksAdapter(context, data, new EnterRemarksAdapter.OnItemClickListener() {
            @Override
            public void onStudentClick(StudentRemarkModel studentModel) {

            }
        });
        studentList = findViewById(R.id.student_list);
        studentList.setAdapter(adapter);

        className = intent.getStringExtra("className");
        testName = intent.getStringExtra("testName");

        ((TextView) findViewById(R.id.className)).setText(className+" - ");
        ((TextView) findViewById(R.id.testName)).setText(testName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Enter Remarks and Attendance", "");

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMarks();
            }
        });
        pb = findViewById(R.id.progressBar);
        //sc = findViewById(R.id.scroll);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        loadData(classId, testId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData(int classId, int testId) {

        pb.setVisibility(View.VISIBLE);
        service.getStudentRemarks(classId, testId, userdata.getAcademicyearId()).enqueue(new Callback<StudentRemarkBean>() {
            @Override
            public void onResponse(Call<StudentRemarkBean> call, Response<StudentRemarkBean> response) {
                StudentRemarkBean resp = response.body();
                pb.setVisibility(View.GONE);

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        // sc.setVisibility(View.VISIBLE);

                        if (resp.data != null) {
                            for (StudentRemarkModel datum : resp.data) {
                                data.add(datum);
                            }
                            adapter.notifyDataSetChanged();
                        } else {

                            Toast.makeText(context, "Could not load student remarks!", Toast.LENGTH_SHORT).show();
                        }
                      /*  data = resp.data;
                        InflateUI();*/

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");

                    } else {
                        Toast.makeText(context, "Could not load student remarks!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StudentRemarkBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                //  sc.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Could not load student remarks!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void InflateUI() {

        for (final StudentRemarkModel studentMark : data) {
            final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.row_enter_remark, studentList, false);
            final EditText edAttendance = view.findViewById(R.id.attendance);
            TextView tvStudentName = view.findViewById(R.id.studentName);
            tvStudentName.setText(studentMark.Name);
            TextView roll = view.findViewById(R.id.rollInEnterRemarksModule);
            roll.setText(String.valueOf(studentMark.Roll == -1 ? "" : studentMark.Roll));

            EditText edRemark = view.findViewById(R.id.remark);
            edRemark.setText(studentMark.Remark == null ? "" : studentMark.Remark);
            edAttendance.setText(studentMark.Attendance == null ? "" : studentMark.Attendance);

            edRemark.setTag(studentMark.Id);
            edAttendance.setTag(studentMark.Id);
            edAttendance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    studentMark.Attendance = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    studentMark.Remark = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            studentList.addView(view);
        }

        pb.setVisibility(View.GONE);
    }*/

    private void saveMarks() {

        StudentRemarkSaveModel model = new StudentRemarkSaveModel();

        model.AcademicYearId = userdata.getAcademicyearId();
        model.SchoolId = userdata.getSchoolId();
        model.CreatedBy = userdata.getUserId();
        model.ClassId = classId;
        model.TestId = testId;

        List<StudentRemarkJsonModel> details = new ArrayList<>();
        for (StudentRemarkModel datum : data) {
            StudentRemarkJsonModel studentMark = new StudentRemarkJsonModel();

            studentMark.StudentId = datum.Id;
            studentMark.Remark = datum.Remark;
            studentMark.Attendance = datum.Attendance;

            details.add(studentMark);
        }

        pb.setVisibility(View.VISIBLE);
        model.Remarks = new Gson().toJson(details);
        service.saveStudentRemarks(model).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (response.body().rcode == Constants.Rcode.OK) {
                    Toast.makeText(context, "Remarks saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Remarks could not be updated. Please try again.", Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Remarks could not be updated. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
