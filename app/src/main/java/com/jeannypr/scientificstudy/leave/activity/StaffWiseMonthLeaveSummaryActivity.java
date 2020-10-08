package com.jeannypr.scientificstudy.leave.activity;
//Created by babulal

import android.app.DatePickerDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityStaffWiseMonthLeaveBinding;
import com.jeannypr.scientificstudy.leave.adapter.StaffWiseMonthLeaveSummaryAdapter;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.MonthLeaveSummaryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.Nullable;

public class StaffWiseMonthLeaveSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    public Context context;
    LeaveService leaveService;
    UserPreference userPreference;
    UserModel userData;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;
    private int selectedMonth, selectedYear;
    SimpleDateFormat df;
    RelativeLayout monthLeaveSummary;
    private TextView datepickerSpinner;
    ArrayList<MonthLeaveSummaryModel> monthLeaves;
    StaffWiseMonthLeaveSummaryAdapter adapter;
    RecyclerView monthWiseLeaveContainer;
    LinearLayout noRecord;
    TextView noRecordMsg;
    ProgressBar pb;
    ActivityStaffWiseMonthLeaveBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_wise_month_leave);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();


        calendar = Calendar.getInstance(TimeZone.getDefault());

        df = new SimpleDateFormat("MM-yyyy");

        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);


        datepickerSpinner = findViewById(R.id.datePicker);
        datepickerSpinner.setText(df.format(calendar.getTime()));

        monthLeaveSummary = findViewById(R.id.monthLeaveSummary);
        monthLeaveSummary.setOnClickListener(this);

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);

        pb = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Staff Month Wise Summary", "");


        monthLeaves = new ArrayList<>();
        monthWiseLeaveContainer = findViewById(R.id.monthWiseLeaveContainer);
        adapter = new StaffWiseMonthLeaveSummaryAdapter(context, monthLeaves);
        monthWiseLeaveContainer.setAdapter(adapter);
        GetTeacherMonthLeave();
        FilterByMonth();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.monthLeaveSummary:
                DatePickerDialog dialog = new DatePickerDialog(StaffWiseMonthLeaveSummaryActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                calendar.set(Calendar.MONTH, monthOfYear);
                             //   calendar.set(Calendar.YEAR, year);
                                selectedMonth = monthOfYear + 1;
                             //   selectedYear = year;
                                datepickerSpinner.setText(df.format(calendar.getTime()));
                                //month = String.valueOf(selectedMonth);
                                FilterByMonth();
                                GetTeacherMonthLeave();
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
//                dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                dialog.show();
        }
    }

    private void GetTeacherMonthLeave() {
        //  pb.setVisibility(View.VISIBLE);
        monthLeaves.add(new MonthLeaveSummaryModel("Sanjay", "1", "01/Jan/2018", "01"));
        monthLeaves.add(new MonthLeaveSummaryModel("Ram Kumar", "2", "10/Oct/2018", "10"));
        monthLeaves.add(new MonthLeaveSummaryModel("Sunil Saini", "5", "12/Sept/2018", "09"));
        monthLeaves.add(new MonthLeaveSummaryModel("Naveen Kumar", "1", "21/Jul/2018", "07"));
        monthLeaves.add(new MonthLeaveSummaryModel("Rakesh Sharma", "3", "30/Nov/2018", "11"));
        monthLeaves.add(new MonthLeaveSummaryModel("Dinesh Sharma", "3", "30/May/2018", "03"));
        /*leaveService.GetTeacherLeaveMonthSummary(selectedMonth, userData.getSchoolId(), userData.getAcademicyearId())
                .enqueue(new Callback<TeacherMonthLeaveSummaryBean>() {

                    @Override
                    public void onResponse(Call<MonthLeaveSummaryBean> call, Response<MonthLeaveSummaryBean> response) {
                        MonthLeaveSummaryBean resp = response.body();
                        if (resp != null) {
                            monthLeaves.clear();

                            if (resp.rcode == Constants.Rcode.OK) {
                                if (resp.data != null) {

                                    for (MonthLeaveSummaryModel datum : resp.data) {
                                        monthLeaves.add(datum);
                                    }
                                    adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<MonthLeaveSummaryBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }

                });*/


    }

    private void FilterByMonth() {
        ArrayList<MonthLeaveSummaryModel> monthLeaveList = new ArrayList<>();


        for (MonthLeaveSummaryModel model : monthLeaves) {
            int monthId = Integer.valueOf(model.getMonthId());
            if (monthId == selectedMonth) {
                monthLeaveList.add(model);
            }
            adapter.FilterSatffName(monthLeaveList);
        }

    }

}
