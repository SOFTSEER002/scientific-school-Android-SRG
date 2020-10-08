package com.jeannypr.scientificstudy.Exam.activity;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.ExamRoasterAdapter;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.ExamRoasterBean;
import com.jeannypr.scientificstudy.Exam.model.ExamRoasterModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.ActivityExamRoasterBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamRoaster extends AppCompatActivity {

    private ExamService examService;
    private Context context;
    UserModel userdata;
    UserPreference userPref;
    ChildModel childModel;
    int examId, classId;
    String examName;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ProgressBar pb;
    List<ExamRoasterModel> examRoasterModels;
    ExamRoasterAdapter roasterAdapter;
    RecyclerView roaster;
    ActivityExamRoasterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_roaster);

        userPref = UserPreference.getInstance(context);
        userdata = userPref.getUserData();
        childModel = userPref.getSelectedChild();

        examService = new DataRepo<>(ExamService.class, context).getService();
        examId = getIntent().getIntExtra("examId", -1);
        classId = getIntent().getIntExtra("classId", -1);
        examName = getIntent().getStringExtra("examName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        roaster = findViewById(R.id.roaster);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(examName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        examRoasterModels = new ArrayList<>();
        roasterAdapter = new ExamRoasterAdapter(context, examRoasterModels);
        roaster.setAdapter(roasterAdapter);
        GetExamRoaster();
    }

    public void GetExamRoaster() {
        pb.setVisibility(View.VISIBLE);

        examService.GetExamDatesAndMarks(examId, classId, childModel.StudentId, userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<ExamRoasterBean>() {
            @Override
            public void onResponse(Call<ExamRoasterBean> call, Response<ExamRoasterBean> response) {
                ExamRoasterBean examRoasterBean = response.body();
                pb.setVisibility(View.GONE);

                if (examRoasterBean != null) {
                    if (examRoasterBean.rcode.equals(Constants.Rcode.OK)) {

                        int size = examRoasterBean.data.size();
                        examRoasterModels.clear();
                        if (size > 0) {
                            for (ExamRoasterModel roaster : examRoasterBean.data) {
                                examRoasterModels.add(roaster);
                            }
                            roasterAdapter.notifyDataSetChanged();
                        }

                    } else if (examRoasterBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");

                    } else {
                        Toast.makeText(context, "Could not load exam roaster. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExamRoasterBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Could not load exam roaster. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
