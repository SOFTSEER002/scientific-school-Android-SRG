package com.jeannypr.scientificstudy.Exam.activity;

/*
 * Author : Babulal
 * Date :
 * class Wise Subject Report
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.ClassSubjectAdapter;
import com.jeannypr.scientificstudy.Exam.model.ClassWiseSubjectBean;
import com.jeannypr.scientificstudy.Exam.model.ClassWiseSubjectModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityTeacherSubjectReportBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassSubjectActivity extends AppCompatActivity {
    private Context context;
    //Spinner ddlteahcreList;
    //DropDownAdapter classListAdapter;
    // ArrayList<DropDownModel> classes;
    // DropDownModel selectedClass;
    int classId;
    String className;
    private ClassSubjectAdapter adapter;
    private ArrayList<ClassWiseSubjectModel> classSubjectList;
    TeacherService teacherService;
    //BaseService baseService;
    UserPreference userPref;
    UserModel userData;
    private LinearLayout noRecord;
    RecyclerView teacherContainer;
    private TextView noRecordMsg, teacherLbl, subjectLbl;
    private ProgressBar pb;
    private ActivityTeacherSubjectReportBinding binding;
    RelativeLayout teacherList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_subject_report);
        context = this;

        // baseService = new DataRepo<>(BaseService.class, context).getService();
        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        classSubjectList = new ArrayList<>();
        adapter = new ClassSubjectAdapter(context, classSubjectList);

        classId = getIntent().getIntExtra("classId", -1);
        className = getIntent().getStringExtra("className");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Class Wise Subject Report", className);

        teacherList = findViewById(R.id.teacherList);
        teacherList.setVisibility(View.GONE);
        teacherLbl = findViewById(R.id.leftLbl);
        teacherLbl.setText("Teacher");

        subjectLbl = findViewById(R.id.rightLbl);
        subjectLbl.setText("Subject");

        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        teacherContainer = findViewById(R.id.teacherContainer);

        teacherContainer.setAdapter(adapter);
        teacherContainer.setLayoutManager(new LinearLayoutManager(this));
        GetClassSubject();
/*
        classes = new ArrayList<>();
        classListAdapter = new DropDownAdapter(context, R.layout.row_spinner, classes);
        ddlteahcreList = findViewById(R.id.ddlteahcreList);

        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Class");
        classes.add(model);
        ddlteahcreList.setAdapter(classListAdapter);*/


        /*ddlteahcreList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedClass = classListAdapter.getItem(position);
                if (selectedClass != null) {
                    if (selectedClass.getId() == 0) {
                        selectedClass = null;
                        classId = 0;
                        className = "";

                    } else {
                        classId = selectedClass.getId();
                        className = selectedClass.getText();
                        GetClassSubject();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        // GetClasses();
    }

    /*private void GetClasses() {

        baseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            classes.add(dropDownModel);
                        }
                        classListAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {

                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void GetClassSubject() {
        pb.setVisibility(View.VISIBLE);

        teacherService.GetClassSubject(classId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassWiseSubjectBean>() {
            @Override
            public void onResponse(Call<ClassWiseSubjectBean> call, Response<ClassWiseSubjectBean> response) {

                ClassWiseSubjectBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        classSubjectList.clear();

                        if (resp.data != null) {
                            for (ClassWiseSubjectModel model : resp.data) {
                                classSubjectList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            noRecord.setVisibility(View.GONE);
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No Record");
                    } else {
                        Toast.makeText(context, "No record found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Somthing went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClassWiseSubjectBean> call, Throwable t) {
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
