package com.jeannypr.scientificstudy.Exam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.EnterMarksAdapter;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.ExamMarkDetailModel;
import com.jeannypr.scientificstudy.Exam.model.GradeModel;
import com.jeannypr.scientificstudy.Exam.model.MarkResponseBean;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkBean;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkJsonModel;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkModel;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkSaveModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterMarksActivity extends AppCompatActivity implements View.OnClickListener {

    private int classId;
    private int subjectId;
    private int testId;
    private ExamService service;
    private Context context;
    UserModel userData;
    LayoutInflater inflater;
    RecyclerView studentList;
    List<StudentMarkModel> data;
    ExamMarkDetailModel examDetail;
    List<GradeModel> grades;
    FloatingActionButton btnSave;
    TextView tvFullMarks, tvPassMarks;
    ProgressBar pb;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ArrayList<DropDownModel> gradeList;
    EnterMarksAdapter enterMarksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_enter_marks);

        Intent intent = getIntent();
        userData = UserPreference.getInstance(context).getUserData();
        classId = intent.getIntExtra("classId", 23);
        subjectId = intent.getIntExtra("subjectId", 3);
        testId = intent.getIntExtra("testId", 21);

        String className = intent.getStringExtra("className");
        String subjectName = intent.getStringExtra("subjectName");
        String testName = intent.getStringExtra("testName");

        inflater = LayoutInflater.from(context);
        service = new DataRepo<>(ExamService.class, context).getService();

        ((TextView) findViewById(R.id.testName)).setText(testName);
        tvFullMarks = findViewById(R.id.fullMarks);
        tvPassMarks = findViewById(R.id.passMarks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String subTitle = className + " (" + subjectName + ")";
        Utility.SetToolbar(context, "Enter Marks", subTitle);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        gradeList = new ArrayList<>();
        data = new ArrayList<>();
        examDetail = new ExamMarkDetailModel();
        grades = new ArrayList<>();
        studentList = findViewById(R.id.student_list);

        loadData(classId, subjectId, testId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                saveMarks();
                break;
        }
    }

    private void loadData(int classId, int subjectId, int testId) {

        pb.setVisibility(View.VISIBLE);
        service.getStudentMarks(classId, subjectId, testId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<StudentMarkBean>() {
            @Override
            public void onResponse(Call<StudentMarkBean> call, Response<StudentMarkBean> response) {
                StudentMarkBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        //  sc.setVisibility(View.VISIBLE);


                        if (resp.data != null && resp.marksDetail != null && resp.grades != null) {
                            if (resp.data.size() > 0) {
                                examDetail = resp.marksDetail;
                                grades.clear();
                                data.clear();

                                for (GradeModel grade : resp.grades) {
                                    grades.add(grade);
                                }
                                for (StudentMarkModel datum : resp.data) {
                                    data.add(datum);
                                }

                                if (!examDetail.getMarking()) {
                                    gradeList.clear();

                                    DropDownModel defaultOption = new DropDownModel();
                                    defaultOption.setText("");
                                    defaultOption.setId(-1);
                                    gradeList.add(defaultOption);

                                    for (GradeModel grade : grades) {
                                        DropDownModel studentGrade = new DropDownModel();
                                        studentGrade.setId(grade.Id);
                                        studentGrade.setText(grade.Grade);
                                        gradeList.add(studentGrade);
                                    }
                                }
                                InflateUI();

                                enterMarksAdapter = new EnterMarksAdapter(context, data, examDetail, grades, gradeList, new EnterMarksAdapter.OnItemClickListener() {

                                    @Override
                                    public void onStudentClick(StudentMarkModel studentModel) {

                                    }
                                });

                                studentList.setAdapter(enterMarksAdapter);
                                enterMarksAdapter.notifyDataSetChanged();
                            } else {
                                btnSave.hide();
                                Toast.makeText(context, "No student found", Toast.LENGTH_SHORT).show();
                            }

                            pb.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(context, getString(R.string.couldNotLoadMarks), Toast.LENGTH_SHORT).show();
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(getString(R.string.noRecordMsg));

                    } else {
                        Toast.makeText(context, getString(R.string.couldNotLoadMarks), Toast.LENGTH_SHORT).show();
                    }
                }
                pb.setVisibility(View.GONE);
                btnSave.show();
            }

            @Override
            public void onFailure(Call<StudentMarkBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                btnSave.show();
                Toast.makeText(context, getString(R.string.couldNotLoadMarks), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InflateUI() {
        tvFullMarks.setText("FM - " + examDetail.FullMarks);
        tvPassMarks.setText("PM - " + examDetail.PassMarks);
    }

    private void saveMarks() {
        if (examDetail.getFreezed()) {
            Toast.makeText(context, getString(R.string.marksFreezedMsg), Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        btnSave.hide();
        Boolean isvalid = true;

        StudentMarkSaveModel model = new StudentMarkSaveModel();
        model.Id = examDetail.StudentTestDetailId;
        model.AcademicYearId = userData.getAcademicyearId();
        model.SchoolId = userData.getSchoolId();
        model.CreatedBy = userData.getUserId();
        model.ClassId = classId;
        model.SubjectId = subjectId;
        model.ScheduledTestId = testId;
        model.TotalMarks = examDetail.FullMarks;
        model.IsMarking = examDetail.getMarking();
        model.TimeStamp = examDetail.timeStamp;

        List<StudentMarkJsonModel> details = new ArrayList<>();
        int counter = 0;
        for (StudentMarkModel datum : data) {
            StudentMarkJsonModel studentMark = new StudentMarkJsonModel();
            studentMark.Id = datum.Id;

            studentMark.IsPresent = datum.IsPresent;
            if (!examDetail.getMarking()) {
                studentMark.Grade = datum.Grade;
                studentMark.GradeId = datum.GradeId;

                if (studentMark.GradeId != null) {
                    counter++;
                }

            } else {
                studentMark.MarksObtained = datum.Marks;
                if (datum.Marks != null) {
                    if (datum.Marks > examDetail.FullMarks) {
                        isvalid = false;
                        EditText editText = studentList.findViewWithTag(studentMark.Id);
                        if (editText != null) {
                            editText.requestFocus();
                        }
                        break;
                    } else {
                        counter++;
                    }
                }
            }

            if (!studentMark.IsPresent) {
                if (datum.Notes == null || datum.Notes.equals("")) {
                    studentMark.Notes = "AB";
                } else {
                    studentMark.Notes = datum.Notes;
                }
                counter++;
            }

            details.add(studentMark);
        }
       /* int thresholdForDataEntry = ((data.size()) * Constants.THRESHOLD_MARKS_ENTRY) / 100;
        if (counter < thresholdForDataEntry) {
            Toast.makeText(context, "You have to enter data for atleast " + thresholdForDataEntry + " students.", Toast.LENGTH_LONG).show();
            return;
        }*/

        if (!isvalid) {
            Toast.makeText(context, "Marks can not be greater than the full marks", Toast.LENGTH_LONG).show();
            pb.setVisibility(View.GONE);
            btnSave.show();
            return;
        }

//        pb.setVisibility(View.VISIBLE);
//        btnSave.hide();
        model.StudentsArr = new Gson().toJson(details);

        service.saveStudentMarks(model).enqueue(new Callback<MarkResponseBean>() {
            @Override
            public void onResponse(Call<MarkResponseBean> call, Response<MarkResponseBean> response) {
                if (response.body() != null) {
                    MarkResponseBean bean = response.body();
                    if (bean.rcode == Constants.Rcode.OK) {
                        String msg;
                        if (bean.data.getDataAlreadyUpdatedByOther()) {
                            msg = getString(R.string.marksAlreadyUpdated);
                        } else {
                            msg = getString(R.string.marksSuccessMsg);
                        }

                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, getString(R.string.marksNotSavedMsg), Toast.LENGTH_LONG).show();
                    }
                }
//                pb.setVisibility(View.GONE);
//                btnSave.show();
                loadData(classId, subjectId, testId);   //Call api to refresh data.
            }

            @Override
            public void onFailure(Call<MarkResponseBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                btnSave.show();
                Toast.makeText(context, getString(R.string.marksNotSavedMsg), Toast.LENGTH_LONG).show();
            }
        });
    }
}
