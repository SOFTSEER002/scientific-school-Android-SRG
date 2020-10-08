package com.jeannypr.scientificstudy.Exam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.ExamModel;
import com.jeannypr.scientificstudy.Exam.model.SubjectExamBean;
import com.jeannypr.scientificstudy.Exam.model.SubjectModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterMarkSelectClassActivity extends AppCompatActivity implements View.OnClickListener {


    private ExamService service;
    private BaseService BaseService;
    private TeacherService teacherService;
    private Context context;
    ArrayList<DropDownModel> classes, subjects, tests;
    DropDownAdapter classAdapter, subjectAdapter, testAdapter;
    UserModel userdata;
    int classId, subjectId, testId;
    String className, subjectName, testName;
    List<ClassModel> myClasses;
    private ConstraintLayout absentExamBtn, classSubjectBtn, teacherSubjectBtn, studentListBtn, remarksModule;
    ImageView btnGo3, btnGo4, btnGo5, enterMarksIc, remarksIc, absentExamIc, classListIc, tacherIc;
    TextView enterMarksLbl, remarksLbl, absentExamLbl, classListLbl, tacherLbl;
    ConstraintLayout entermarksRow, remarksRow, examAbsentRow, classWiseSubjectRow, teacherWiseSubjectRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_exam_module);
        userdata = UserPreference.getInstance(context).getUserData();
        BaseService = new DataRepo<>(BaseService.class, context).getService();
        service = new DataRepo<>(ExamService.class, context).getService();
        teacherService = new DataRepo<>(TeacherService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Exam", "");

        classes = new ArrayList<>();
        DropDownModel classDefault = new DropDownModel();
        classDefault.setId(-1);
        classDefault.setText("Select Class");
        classes.add(classDefault);
        classAdapter = new DropDownAdapter(context, R.layout.row_spinner, classes);

        InitializeSubAndTests();

        final Spinner classList = findViewById(R.id.classList);
        final Spinner subjectList = findViewById(R.id.subjectList);
        final Spinner testList = findViewById(R.id.testList);

        entermarksRow = findViewById(R.id.entermarksRow);
        remarksRow = findViewById(R.id.remarksRow);
        examAbsentRow = findViewById(R.id.examAbsentRow);
        classWiseSubjectRow = findViewById(R.id.classWiseSubjectRow);
        teacherWiseSubjectRow = findViewById(R.id.teacherWiseSubjectRow);

        entermarksRow.setOnClickListener(this);
        remarksRow.setOnClickListener(this);
        examAbsentRow.setOnClickListener(this);
        classWiseSubjectRow.setOnClickListener(this);
        teacherWiseSubjectRow.setOnClickListener(this);

        remarksIc = findViewById(R.id.remarksIc);
        remarksLbl = findViewById(R.id.remarkstxt);

        if (userdata.getRoleTitle().equals(Constants.Role.TEACHER) && (userdata.getClassId() == null || userdata.getClassId() == -1)) {
            remarksIc.setVisibility(View.GONE);
            remarksLbl.setVisibility(View.GONE);
            findViewById(R.id.remarksDiv).setVisibility(View.GONE);
        }

        classList.setAdapter(classAdapter);
        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = classAdapter.getItem(position);
                if (selectedItem != null) {

                    if (selectedItem.getId() == -1) {
                        classId = 0;
                        className = "";

                    } else {
                        classId = selectedItem.getId();
                        className = selectedItem.getText();
                        String mode = userdata.getRoleTitle().equals(Constants.Role.ADMIN) ? Constants.MODE.ALL : Constants.MODE.ASSIGNED;

                        subjects.clear();
                        subjectAdapter.notifyDataSetChanged();
                        subjectId = 0;
                        subjectName = "";

                        tests.clear();
                        testAdapter.notifyDataSetChanged();
                        testId = 0;
                        testName = "";

                        DropDownModel subjectDefault = new DropDownModel();
                        subjectDefault.setId(-1);
                        subjectDefault.setText("Select Subject");
                        subjects.add(subjectDefault);

                        DropDownModel testDefault = new DropDownModel();
                        testDefault.setId(-1);
                        testDefault.setText("Select Exam");
                        tests.add(testDefault);

                        service.getSubjectsAndExams(classId, userdata.getUserId(), mode, userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<SubjectExamBean>() {
                            @Override
                            public void onResponse(Call<SubjectExamBean> call, Response<SubjectExamBean> response) {
                                SubjectExamBean resp = response.body();
                                if (resp != null) {

                                    if (resp.rcode == Constants.Rcode.OK) {

                                        for (SubjectModel subject : resp.subjects) {
                                            DropDownModel sub = new DropDownModel();
                                            sub.setId(subject.Id);
                                            sub.setText(subject.Name);
                                            subjects.add(sub);
                                        }
                                        subjectAdapter.notifyDataSetChanged();


                                        for (ExamModel exam : resp.exams) {
                                            DropDownModel ex = new DropDownModel();
                                            ex.setId(exam.Id);
                                            ex.setText(exam.Name);
                                            tests.add(ex);
                                        }
                                        testAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(context, "Unable to load subjects and exams. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SubjectExamBean> call, Throwable t) {
                                Toast.makeText(context, "Unable to load subjects and exams. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


        subjectList.setAdapter(subjectAdapter);
        subjectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = subjectAdapter.getItem(position);
                if (selectedItem.getId() == -1) {
                    subjectId = 0;
                    subjectName = "";
                } else {
                    subjectId = selectedItem.getId();
                    subjectName = selectedItem.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


        testList.setAdapter(testAdapter);
        testList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                DropDownModel selectedItem = testAdapter.getItem(position);
                if (selectedItem.getId() == -1) {
                    testId = 0;
                    testName = "";
                } else {
                    testId = selectedItem.getId();
                    testName = selectedItem.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


        if (userdata.getRoleTitle().equals(Constants.Role.ADMIN)) {
            BaseService.getClasses(userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean classBean = response.body();
                    if (classBean != null) {

                        if (classBean.rcode == Constants.Rcode.OK) {
                            for (ClassModel datum : classBean.data) {
                                DropDownModel cls = new DropDownModel();
                                cls.setId(datum.Id);
                                cls.setText(datum.Name);
                                classes.add(cls);
                            }
                            classAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                }
            });
        } else if (userdata.getRoleTitle().equals(Constants.Role.TEACHER)) {
            //   int teacherId = userdata.UserId;

            teacherService.GetMyClasses(userdata.getSchoolId(), userdata.getAcademicyearId(), userdata.getUserId()).enqueue(new Callback<ClassBean>() {
                @Override
                public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                    ClassBean myClassesBean = response.body();
                    if (myClassesBean.rcode == Constants.Rcode.OK) {
                        myClasses = myClassesBean.data;
                        for (ClassModel myClass : myClasses) {
                            DropDownModel cls = new DropDownModel();
                            cls.setId(myClass.Id);
                            cls.setText(myClass.Name);
                            classes.add(cls);
                        }
                        classAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ClassBean> call, Throwable t) {
                    Toast.makeText(context, "Could not load class. Please try again", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void InitializeSubAndTests() {

        subjects = new ArrayList<>();
        DropDownModel subjectDefault = new DropDownModel();
        subjectDefault.setId(-1);
        subjectDefault.setText("Select Subject");
        subjects.add(subjectDefault);
        subjectAdapter = new DropDownAdapter(context, R.layout.row_spinner, subjects);

        tests = new ArrayList<>();
        DropDownModel testDefault = new DropDownModel();
        testDefault.setId(-1);
        testDefault.setText("Select Exam");
        tests.add(testDefault);
        testAdapter = new DropDownAdapter(context, R.layout.row_spinner, tests);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.examAbsentRow:
                if ((classId != 0 && subjectId != 0 && testId != 0) || ((classId != 0) && (subjectId != 0))) {
                    Intent absentIntent = new Intent(context, AbsentReportExamActivity.class);
                    absentIntent.putExtra("classId", classId);
                    absentIntent.putExtra("subjectId", subjectId);
                    absentIntent.putExtra("testId", testId);
                    absentIntent.putExtra("className", className);
                    absentIntent.putExtra("subjectName", subjectName);
                    absentIntent.putExtra("testName", testName);
                    startActivity(absentIntent);
                } else {
                    Toast.makeText(context, "Please select class, subject to proceed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.classWiseSubjectRow:
                if (classId != 0) {
                    Intent classIntent = new Intent(context, ClassSubjectActivity.class);
                    classIntent.putExtra("classId", classId);
                    classIntent.putExtra("className", className);
                    startActivity(classIntent);
                } else {
                    Toast.makeText(context, "Please select class", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.teacherWiseSubjectRow:
                Intent teacherIntent = new Intent(context, TeacherSubjectActivity.class);
                startActivity(teacherIntent);
                break;

            case R.id.entermarksRow:
                if (classId != 0 && subjectId != 0 && testId != 0) {
                    Intent examModuleIntent = new Intent(context, EnterMarksActivity.class);
                    examModuleIntent.putExtra("classId", classId);
                    examModuleIntent.putExtra("subjectId", subjectId); //57
                    examModuleIntent.putExtra("testId", testId);
                    examModuleIntent.putExtra("className", className);
                    examModuleIntent.putExtra("subjectName", subjectName);
                    examModuleIntent.putExtra("testName", testName);
                    startActivity(examModuleIntent);
                } else {
                    Toast.makeText(context, "Please select a class, subject and exam to proceed!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.remarksRow:
                if (classId != 0 && testId != 0) {
                    Intent examModuleIntent = new Intent(context, EnterRemarksActivity.class);
                    examModuleIntent.putExtra("classId", classId);
                    examModuleIntent.putExtra("testId", testId);
                    examModuleIntent.putExtra("className", className);
                    examModuleIntent.putExtra("testName", testName);
                    startActivity(examModuleIntent);
                } else {
                    Toast.makeText(context, "Please select a class, and exam to proceed!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}