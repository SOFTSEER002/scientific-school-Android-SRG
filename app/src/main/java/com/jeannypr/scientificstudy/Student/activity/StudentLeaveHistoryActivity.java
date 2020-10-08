package com.jeannypr.scientificstudy.Student.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.Student.adapter.StudentLeaveHistoryAdapter;
import com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityStudentLeaveHistoryBinding;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryBean;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLeaveHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Toolbar toolbar;
    ActivityStudentLeaveHistoryBinding binding;
    LeaveService leaveService;
    MovableFloatingActionButton requestBtn;
    ProgressBar pb;
    UserModel userModel;
    UserPreference userPref;
    ChildModel childModel;
    ArrayList<LeaveHistoryModel> leaveLogs;
    LinearLayout noRecordRow;
    TextView noRecordMsg, totalLeavesTxt;
    StudentLeaveHistoryAdapter adapter;
    RecyclerView leaveHistory;
    RelativeLayout subHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_leave_history);
        context = this;

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        childModel = userPref.getSelectedChild();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();

        leaveLogs = new ArrayList<>();
        adapter = new StudentLeaveHistoryAdapter(context, leaveLogs);
        leaveHistory = findViewById(R.id.leaveHistory);
        leaveHistory.setAdapter(adapter);

        pb = findViewById(R.id.pb);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecordRow = findViewById(R.id.noRecord);
        totalLeavesTxt = findViewById(R.id.totalLeavesVal);
        subHeader = findViewById(R.id.subHeader);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Leave", "");

        requestBtn = findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(this);

        GetData();
    }

    private void GetData() {
        pb.setVisibility(View.VISIBLE);

        leaveService.GetStudentLeaveHistory(userModel.getSchoolId(), userModel.getAcademicyearId(), childModel.StudentId).enqueue(new Callback<LeaveHistoryBean>() {
            @Override
            public void onResponse(Call<LeaveHistoryBean> call, Response<LeaveHistoryBean> response) {
                LeaveHistoryBean bean = response.body();
                leaveLogs.clear();

                if (bean.rcode != null) {
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data.size() > 0) {
                            double count = 0.0;

                            for (LeaveHistoryModel datum : bean.data) {
                                leaveLogs.add(datum);
                                count += Double.parseDouble(datum.TotalRequestedDays);
                            }
                            adapter.notifyDataSetChanged();

                            totalLeavesTxt.setText(Double.toString(count));

                        } else {
                            subHeader.setVisibility(View.GONE);
                        }


                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        Log.e("Leave History: ", bean.msg);
                        noRecordMsg.setText("No record found");
                        noRecordRow.setVisibility(View.VISIBLE);
                        subHeader.setVisibility(View.GONE);
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LeaveHistoryBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong.Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Student leave history:", t.getMessage());
                subHeader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.requestBtn:
                Intent requestLeaveIntent = new Intent(context, RequestStudentLeaveActivity.class);
                startActivity(requestLeaveIntent);
                break;
        }
    }
}
