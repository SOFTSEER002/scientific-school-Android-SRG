package com.jeannypr.scientificstudy.leave.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.leave.adapter.SelectLeaveDaysAdapter;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.RequestedLeaveDaysModel;
import com.jeannypr.scientificstudy.leave.model.SchoolHolidaysBean;
import com.jeannypr.scientificstudy.leave.model.SchoolHolidaysModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLeaveDaysActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Toolbar toolbar;
    private static final String TAG = SelectLeaveDaysActivity.class.getSimpleName();
    private boolean isFullday = false;
    String subTitle, selectedDates;
    ConstraintLayout leaveDayRow;
    ConstraintLayout slider;
    TextView sliderLbl, nextBtn, totalDaysTxt, selectedDatesTxt;
    FloatingActionButton swipeBtn;
    ArrayList<RequestedLeaveDaysModel> requestedLeaveDatesArr;
    RecyclerView requestedDaysList;
    SelectLeaveDaysAdapter adapter;
    LeaveService leaveService;
    double totalDays;
    UserPreference userPref;
    UserModel userModel;
    ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_leave_days);

        context = this;
        leaveService = new DataRepo<>(LeaveService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        requestedLeaveDatesArr = new ArrayList<>();

        selectedDates = getIntent().getStringExtra("selectedDates");
        totalDays = getIntent().getDoubleExtra("totalDays", 0.0);

        requestedLeaveDatesArr = getIntent().getParcelableArrayListExtra("leaveDays");

      /*  adapter = new SelectLeaveDaysAdapter(context, requestedLeaveDatesArr, new SelectLeaveDaysAdapter.OnItemClickListner() {
            @Override
            public void OnStatusChange(double totalSelectedDays) {
                totalDays = totalSelectedDays;
                SetToolbar();

            }
        });*/

        //   adapter.notifyDataSetChanged();
        pb = findViewById(R.id.pb);
        totalDaysTxt = findViewById(R.id.totalDays);
        selectedDatesTxt = findViewById(R.id.selectedDates);
        requestedDaysList = findViewById(R.id.requestedDays);
        /*requestedDaysList.setAdapter(adapter);*/

        toolbar = findViewById(R.id.toolbar);

        GetSchoolHolidays();
        SetToolbar();

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SetToolbar() {
        String totalDaysStr;
        if (totalDays > 1) {
            totalDaysStr = " (" + totalDays + " days)";
        } else {
            totalDaysStr = " (" + totalDays + " day)";
        }
        subTitle = selectedDates + totalDaysStr;

        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Select leave days", "");
        totalDaysTxt.setText(Double.toString(totalDays));
        selectedDatesTxt.setText("(" + selectedDates + ")");
    }

    private void GetSchoolHolidays() {
        pb.setVisibility(View.VISIBLE);

        leaveService.GetSchoolHolidays(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<SchoolHolidaysBean>() {
            @Override
            public void onResponse(Call<SchoolHolidaysBean> call, Response<SchoolHolidaysBean> response) {

                if (response.body() != null) {
                    SchoolHolidaysBean bean = response.body();

                    if (bean.data != null && bean.data.size() > 0) {
                        double counter = 0.0;

                        for (RequestedLeaveDaysModel leave : requestedLeaveDatesArr) {
                            for (SchoolHolidaysModel holiday : bean.data) {

                                if (leave.Date.equals(holiday.StartDate)) {
                                    leave.IsHoliday = true;
                                    leave.HolidayTitle = holiday.HolidayTitle;

                                    counter++;
                                    break;

                                } else {
                                    leave.IsHoliday = false;
                                    leave.HolidayTitle = "";
                                }
                            }
                            continue;
                        }
                        totalDays -= counter;
                        requestedLeaveDatesArr.get(0).TotalRequestedDays = totalDays;
                        SetToolbar();

                        adapter = new SelectLeaveDaysAdapter(context, requestedLeaveDatesArr, new SelectLeaveDaysAdapter.OnItemClickListner() {
                            @Override
                            public void OnStatusChange(double totalSelectedDays) {
                                totalDays = totalSelectedDays;
                                SetToolbar();

                            }
                        });
                        requestedDaysList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        adapter = new SelectLeaveDaysAdapter(context, requestedLeaveDatesArr, new SelectLeaveDaysAdapter.OnItemClickListner() {
                            @Override
                            public void OnStatusChange(double totalSelectedDays) {
                                totalDays = totalSelectedDays;
                                SetToolbar();

                            }
                        });
                        requestedDaysList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SchoolHolidaysBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Log.e("Holidays list : ", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextBtn:
                RemoveHolidaysFromList();
                break;
        }
    }

    private void RemoveHolidaysFromList() {
        ArrayList<RequestedLeaveDaysModel> temp = new ArrayList<>();

        for (RequestedLeaveDaysModel leave : requestedLeaveDatesArr) {
            if (!leave.IsHoliday) {
                temp.add(leave);
            }
        }

        Intent requestLeaveIntent = new Intent(context, LeaveModuleActivity.class);
        if (temp.size() > 0) {
            temp.get(0).TotalRequestedDays = totalDays;
            requestLeaveIntent.putParcelableArrayListExtra("leaveDays", temp);
            setResult(RESULT_OK, requestLeaveIntent);

        } else {
          //  requestLeaveIntent.putParcelableArrayListExtra("leaveDays", temp);
            setResult(RESULT_CANCELED, requestLeaveIntent);
        }
        finish();
    }

  /*  public void CreateTabUI() {
        if (requestedLeaveDatesArr.size() > 1) {
            for (String leaveDate : requestedLeaveDatesArr) {

                RelativeLayout tabRow = (RelativeLayout) inflater.inflate(R.layout.row_selected_leave_days, tabContainer, false);
                TextView txtDateTab = tabRow.findViewById(R.id.dateTab);
                final ToggleButton txtFullHalfTab = tabRow.findViewById(R.id.fullHalfTab);

               *//* txtFullHalfTab.setChecked(true);
                txtFullHalfTab.setTextOn("@string/fulldayToggleBtn");*//*

                txtDateTab.setText(leaveDate);

                txtFullHalfTab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            txtFullHalfTab.setTextOn("@string/fulldayToggleBtn");
                            txtFullHalfTab.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_green_bg));
                        } else {
                            txtFullHalfTab.setTextOff("@string/halfdayToggleBtn");
                            txtFullHalfTab.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_theme_bg));
                        }
                    }
                });

                tabContainer.addView(tabRow);
            }
        }
    }*/
}
