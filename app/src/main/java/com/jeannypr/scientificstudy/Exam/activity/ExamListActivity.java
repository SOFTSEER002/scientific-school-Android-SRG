package com.jeannypr.scientificstudy.Exam.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
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
import com.jeannypr.scientificstudy.Exam.adapter.ExamListAdapter;
import com.jeannypr.scientificstudy.Exam.api.ExamService;
import com.jeannypr.scientificstudy.Exam.model.ExamListBean;
import com.jeannypr.scientificstudy.Exam.model.ExamListModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityExamListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamListActivity extends AppCompatActivity implements View.OnClickListener {

    private ExamService examService;
    private Context context;
    UserModel userData;
    UserPreference userPref;
    ChildModel childModel;
    int classId;
    private LinearLayout noRecord;
    private TextView noRecordMsg;
    ProgressBar pb;
    ExamListModel exams;
    List<ExamListModel> upcomingExams;
    List<ExamListModel> pastExams;
    RecyclerView upcomingExamList, pastExamList;
    ExamListAdapter upcomingExamsAdapter, pastExamsAdapter;
    ActivityExamListBinding binding;
    Group group;
    TextView pastExamHeader, upcomingExamHeader;
    View divider, divider2;
    TextView reportCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_list);
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        childModel = userPref.getSelectedChild();
        classId = childModel.ClassId;

        examService = new DataRepo<>(ExamService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        pb = findViewById(R.id.progressBar);
        upcomingExamList = findViewById(R.id.upcomingExamList);
        pastExamList = findViewById(R.id.pastExamList);
        upcomingExamHeader = findViewById(R.id.upcomingExamHeader);
        pastExamHeader = findViewById(R.id.pastExamHeader);
        divider = findViewById(R.id.divider);
        divider2 = findViewById(R.id.divider2);

        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Exams", "");

        upcomingExams = new ArrayList<>();
        pastExams = new ArrayList<>();

        upcomingExamsAdapter = new ExamListAdapter(context, upcomingExams, new ExamListAdapter.OnItemClickListener() {
            @Override
            public void onExamClick(ExamListModel examListModel) {
                Intent examRoaster = new Intent(context, ExamRoaster.class);
                examRoaster.putExtra("examId", examListModel.Id);
                examRoaster.putExtra("examName", examListModel.TestName);
                examRoaster.putExtra("classId", classId);
                startActivity(examRoaster);
            }
        });

        pastExamsAdapter = new ExamListAdapter(context, pastExams, new ExamListAdapter.OnItemClickListener() {

            @Override
            public void onExamClick(ExamListModel examListModel) {
                Intent examRoaster = new Intent(context, ExamRoaster.class);
                examRoaster.putExtra("examId", examListModel.Id);
                examRoaster.putExtra("examName", examListModel.TestName);
                examRoaster.putExtra("classId", classId);
                startActivity(examRoaster);
            }
        });

        upcomingExamList.setAdapter(upcomingExamsAdapter);
        pastExamList.setAdapter(pastExamsAdapter);

        GetExamList();
    }

    public void GetExamList() {
        pb.setVisibility(View.VISIBLE);
//        group.setVisibility(View.GONE);

        examService.GetExamsList(classId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ExamListBean>() {
            @Override
            public void onResponse(Call<ExamListBean> call, Response<ExamListBean> response) {
                ExamListBean examListBean = response.body();
                pb.setVisibility(View.GONE);
                //group.setVisibility(View.VISIBLE);

                if (examListBean != null) {
                    if (examListBean.rcode.equals(Constants.Rcode.OK)) {

                        exams = examListBean.data;
                        int upcomingExamsSize = exams.UpcomingExams.size();
                        if (upcomingExamsSize > 0) {

                            for (ExamListModel upcomingExam : exams.UpcomingExams) {
                                upcomingExams.add(upcomingExam);
                            }
                            upcomingExamsAdapter.notifyDataSetChanged();

                        } else {
                            upcomingExamHeader.setVisibility(View.GONE);
                            divider.setVisibility(View.GONE);
                        }

                        int pastExamsSize = exams.PreviousExams.size();
                        if (pastExamsSize > 0) {
                            for (ExamListModel previousExam : exams.PreviousExams) {
                                pastExams.add(previousExam);
                            }
                            pastExamsAdapter.notifyDataSetChanged();

                        } else {
                            pastExamHeader.setVisibility(View.GONE);
                            divider2.setVisibility(View.GONE);
                        }

                    } else if (examListBean.rcode == Constants.Rcode.NORECORDS) {
                        pastExamHeader.setVisibility(View.GONE);
                        divider2.setVisibility(View.GONE);
                        upcomingExamHeader.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record found.");

                    } else {
                        Toast.makeText(context, "Could not load exam list. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExamListBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Could not load exam list. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
       /* Intent intent = new Intent(context, HelpActivity.class);
        intent.putExtra("webUrl", Constants.ADMIN_TEACHER_HELP_URL);
        startActivity(intent);*/
    }
}
