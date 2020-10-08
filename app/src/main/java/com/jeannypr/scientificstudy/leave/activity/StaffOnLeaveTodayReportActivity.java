package com.jeannypr.scientificstudy.leave.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Exam.adapter.StaffOnLeaveTodayAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityLeaveReportBinding;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveBean;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Created by babulal
public class StaffOnLeaveTodayReportActivity extends AppCompatActivity {
    public Context context;
    LeaveService leaveService;
    UserPreference userPreference;
    UserModel userData;
    private SimpleDateFormat df;
    Calendar calendar;
    RecyclerView staffLeaveContainer;
    private ArrayList<MonthWiseLeaveModel> staffLeaveList;
    private StaffOnLeaveTodayAdapter adapter;
    RelativeLayout subheader, leaveMonthSpinner;
    LinearLayout noRecord;
    TextView noRecordMsg;
    String CurrentDate;
    private ProgressBar pb;
    ActivityLeaveReportBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leave_report);
        // setContentView(R.layout.activity_leave_report);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();

      /*  subheader = findViewById(R.id.subheader);
        subheader.setVisibility(View.GONE);*/

        leaveMonthSpinner = findViewById(R.id.leaveMonthSpinner);
        leaveMonthSpinner.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Staff On Leave Today Report", "");

        df = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        CurrentDate = formatter.format(todayDate);

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);

        pb = findViewById(R.id.progressBar);

        staffLeaveList = new ArrayList<>();
        staffLeaveContainer = findViewById(R.id.leaveRequest);
        adapter = new StaffOnLeaveTodayAdapter(context, staffLeaveList);
        staffLeaveContainer.setAdapter(adapter);
        staffLeaveContainer.setLayoutManager(new LinearLayoutManager(this));

        GetStaffOnLeaveList();
    }

    private void GetStaffOnLeaveList() {
        pb.setVisibility(View.VISIBLE);

        leaveService.GetOnLeaveToday(CurrentDate, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<MonthWiseLeaveBean>() {
            @Override
            public void onResponse(Call<MonthWiseLeaveBean> call, Response<MonthWiseLeaveBean> response) {
                if (response.body() != null) {
                    MonthWiseLeaveBean resp = response.body();

                    if (resp.rcode == Constants.Rcode.OK) {
                        staffLeaveList.clear();
                        if (resp.data != null) {

                            int size = resp.data.size();
                            if (size > 0) {

                                for (MonthWiseLeaveModel model : resp.data) {
                                    staffLeaveList.add(model);
                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record Found");
                            }
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record Found");

                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }

                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthWiseLeaveBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Leave summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
