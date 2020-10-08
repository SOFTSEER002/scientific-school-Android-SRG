package com.jeannypr.scientificstudy.Exam.activity;

/*
 * Author : Babulal
 * Date :
 * Absent report Exam
 */

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.AbsentReportExamAdapter;
import com.jeannypr.scientificstudy.Exam.model.AbsentExamBean;
import com.jeannypr.scientificstudy.Exam.model.AbsentExamModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityAbsentReportExamBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsentReportExamActivity extends AppCompatActivity {
    private Context context;
    private ActivityAbsentReportExamBinding binding;
    TeacherService teacherService;
    UserModel userData;
    UserPreference userPreference;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    private ArrayList<AbsentExamModel> absentExamList;
    private AbsentReportExamAdapter adapter;
    RecyclerView studentListContainer;
    private ProgressBar pb;
    int classId = 0, subjectId = 0, testId = 0;
    String className, subjectName, subTitle, testName;
    TextView txtsubjectName, txtExam, subjectLbl, examLbl, subHeaderTitle;
    ConstraintLayout subheader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_absent_report_exam);
        context = this;

        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();

        classId = getIntent().getIntExtra("classId", -1);
        className = getIntent().getStringExtra("className");
        subjectId = getIntent().getIntExtra("subjectId", -1);
        subjectName = getIntent().getStringExtra("subjectName");
        testId = getIntent().getIntExtra("testId", -1);
        testName = getIntent().getStringExtra("testName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        // subheader = findViewById(R.id.subheader);
        subHeaderTitle = findViewById(R.id.subTitle);

        if (subjectName != null && !subjectName.equals("") && testName != null && !testName.equals("")) {
            subHeaderTitle.setText(subjectName + " :-" + testName);
        } else {
            subHeaderTitle.setText(subjectName);
        }

       /* txtExam = findViewById(R.id.txtExam);
        txtsubjectName = findViewById(R.id.txtsubjectName);
        subjectLbl = findViewById(R.id.subject);
        examLbl = findViewById(R.id.exam);*/

        /*if (subjectName != null && !subjectName.equals("")) {
            //subTitle=className+"("+subjectName+")";
            // subheader.setVisibility(View.VISIBLE);
            txtsubjectName.setVisibility(View.VISIBLE);
            subjectLbl.setVisibility(View.VISIBLE);

        } else {
            txtsubjectName.setVisibility(View.GONE);
            subjectLbl.setVisibility(View.GONE);
        }
        if (testName != null && !testName.equals("")) {
            examLbl.setVisibility(View.VISIBLE);
            txtExam.setVisibility(View.VISIBLE);

        } else {
            examLbl.setVisibility(View.GONE);
            txtExam.setVisibility(View.GONE);
            // subheader.setVisibility(View.GONE);
        }*/

        Utility.SetToolbar(context, "Absent Report", className);

        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

      /*  txtExam.setText(testName);
        txtsubjectName.setText(subjectName);*/

        studentListContainer = findViewById(R.id.studentListContainer);

        absentExamList = new ArrayList<>();
        adapter = new AbsentReportExamAdapter(context, absentExamList, subjectName, testName);
        studentListContainer.setAdapter(adapter);
        studentListContainer.setLayoutManager(new LinearLayoutManager(this));

        GetStudentExamList();
    }

    private void GetStudentExamList() {
        pb.setVisibility(View.VISIBLE);

        teacherService.GetExamAbsentList(classId, testId, subjectId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<AbsentExamBean>() {
            @Override
            public void onResponse(Call<AbsentExamBean> call, Response<AbsentExamBean> response) {
                AbsentExamBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        absentExamList.clear();
                        if (resp.data != null) {

                            for (AbsentExamModel datum : resp.data) {
                                absentExamList.add(datum);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No Record");
                    } else {
                        Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Somthing went worng. Please try again", Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AbsentExamBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Report could not be loaded. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}