package com.jeannypr.scientificstudy.Exam.activity;

/*
 * Author : Babulal
 * Date :
 * Teacher Wise Subject Report
 */


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Exam.adapter.TeacherSubjectAdapter;
import com.jeannypr.scientificstudy.Exam.model.TeacherSubjectBean;
import com.jeannypr.scientificstudy.Exam.model.TeacherSubjectModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityTeacherSubjectReportBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherSubjectActivity extends AppCompatActivity {
    private Context context;
    Spinner ddlteahcreList;
    DropDownAdapter teacherListAdapter;
    ArrayList<DropDownModel> teacherList;
    DropDownModel selectedTeacher;
    private TeacherSubjectAdapter adapter;
    private ArrayList<TeacherSubjectModel> teachersData;
    private ProgressBar pb;
    TeacherService teacherService;
    UserPreference userPref;
    UserModel userData;
    private LinearLayout noRecord;
    RecyclerView teacherContainer;
    private TextView noRecordMsg, classLbl, subjectLbl;
    String teacherName;
    int teacherId;
    private ActivityTeacherSubjectReportBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_subject_report);


        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        teachersData = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Teacher Wise Subject Report", "");

        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        teacherContainer = findViewById(R.id.teacherContainer);
        classLbl = findViewById(R.id.leftLbl);
        classLbl.setText("Class");

        subjectLbl = findViewById(R.id.rightLbl);
        subjectLbl.setText("Subject");

        adapter = new TeacherSubjectAdapter(context, teachersData);
        teacherContainer.setAdapter(adapter);
        teacherContainer.setLayoutManager(new LinearLayoutManager(this));

        teacherList = new ArrayList<>();
        teacherListAdapter = new DropDownAdapter(context, R.layout.row_spinner, teacherList);
        ddlteahcreList = findViewById(R.id.ddlteahcreList);

        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Teacher");
        teacherList.add(model);
        ddlteahcreList.setAdapter(teacherListAdapter);

        ddlteahcreList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedTeacher = teacherListAdapter.getItem(position);
                if (selectedTeacher != null) {
                    if (selectedTeacher.getId() == 0) {
//                        selectedTeacher = null;
                        teacherId = 0;
                        teacherName = "";

                    } else {
                        teacherId = selectedTeacher.getId();
                        teacherName = selectedTeacher.getText();
                        GetTeacherSubject();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // GetTeacherSubject();
        GetTeacherList();

    }

    private void GetTeacherList() {
        teacherService.getTeachers(userData.getSchoolId(),userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {

                TeacherBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<TeacherModel> allTeachers = resp.data;

                        for (TeacherModel teacher : allTeachers) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(teacher.Id);
                            dropDownModel.setText(teacher.Name);
                            teacherList.add(dropDownModel);
                        }
                        teacherListAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Teachers could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetTeacherSubject() {
        pb.setVisibility(View.VISIBLE);
        teacherService.GetStaffSubject(teacherId, userData.getSchoolId(), userData.getAcademicyearId())
                .enqueue(new Callback<TeacherSubjectBean>() {

                    @Override
                    public void onResponse(Call<TeacherSubjectBean> call, Response<TeacherSubjectBean> response) {
                        TeacherSubjectBean resp = response.body();
                        if (resp != null) {
                            teachersData.clear();

                            if (resp.rcode == Constants.Rcode.OK) {
                                if (resp.data != null) {

                                    for (TeacherSubjectModel datum : resp.data) {
                                        teachersData.add(datum);
                                    }
                                    adapter.notifyDataSetChanged();
                                    noRecord.setVisibility(View.GONE);
                                }

                            } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record Found");
                            } else {
                                Toast.makeText(context, "No record found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                        }
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<TeacherSubjectBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }

                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
