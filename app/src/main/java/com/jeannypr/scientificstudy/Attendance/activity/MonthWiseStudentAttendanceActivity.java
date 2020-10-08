package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.MonthWiseAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.MonthAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.activity.MonthwiseAttendanceSummaryActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityMonthWiseStudentAttendanceBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthWiseStudentAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    public Context context;
    int classId;
    String className;
    Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private int selectedMonth, selectedYear;
    SimpleDateFormat dateFormat, df2;
    private TextView datepickerSpinner;
    // RelativeLayout monthAttendance;
    UserPreference userPreference;
    UserModel userData;
    List<MonthAttendanceModel> students;
    AttendanceService attendanceService;
    private MonthWiseAttendanceAdapter adapter;
    private RecyclerView monthwiseAttendanceList;
    private ProgressBar pb;
    ActivityMonthWiseStudentAttendanceBinding binding;
    TextView noRecordMsg, datepicker;
    LinearLayout noRecord;
    ConstraintLayout monthAttendance;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   binding = DataBindingUtil.setContentView(this, R.layout.activity_month_wise_teacher_attendance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month_wise_student_attendance);
        context = this;

        classId = getIntent().getIntExtra("classId", -1);
        className = getIntent().getStringExtra("className");

        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();
        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

      /*  TextView halfday = findViewById(R.id.halfday);
        halfday.setVisibility(View.GONE);*/

        pb = findViewById(R.id.progressBar);

        calendar = Calendar.getInstance(TimeZone.getDefault());

        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);

        dateFormat = new SimpleDateFormat("MM-yyyy");
        df2 = new SimpleDateFormat("MMM, yyyy", Locale.ENGLISH);

        datepickerSpinner = findViewById(R.id.datepicker);
        datepickerSpinner.setText(df2.format(calendar.getTime()));
       /* monthAttendance = findViewById(R.id.monthAttendance);
        monthAttendance.setOnClickListener(this);

*/
        datepicker = findViewById(R.id.datepicker);
        datepicker.setOnClickListener(this);

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Month Wise Report", "");
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Month Wise Student Attendance");
            getSupportActionBar().setSubtitle("");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        monthwiseAttendanceList = findViewById(R.id.monthwiseAttendanceList);
        students = new ArrayList<>();
        adapter = new MonthWiseAttendanceAdapter(context, students, new MonthWiseAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MonthAttendanceModel model) {

                if (model.StudentId != 0) {
                    Intent intent = new Intent(context, MonthwiseAttendanceSummaryActivity.class);
                    intent.putExtra("classId", classId);
                    intent.putExtra("schoolId", userData.getSchoolId());
                    intent.putExtra("academicyearId", userData.getAcademicyearId());
                    intent.putExtra("monthId", selectedMonth);
                    intent.putExtra("year", selectedYear);
                    intent.putExtra("present", model.Present);
                    intent.putExtra("absent", model.Absent);
                    intent.putExtra("fullName", model.fullName);
                    intent.putExtra("studentId", model.StudentId);
                    startActivity(intent);
                }

            }
        });
        monthwiseAttendanceList.setAdapter(adapter);
        GetStudentAttendance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //  case R.id.monthAttendance:
            case R.id.datepicker:
                DatePickerDialog dialog = new DatePickerDialog(MonthWiseStudentAttendanceActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.YEAR, year);
                                selectedMonth = monthOfYear + 1;
                                selectedYear = year;
                                datepickerSpinner.setText(df2.format(calendar.getTime()));
                                GetStudentAttendance();
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                //dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N || android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                    dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                }

                dialog.show();
                break;
        }
    }

    private void GetStudentAttendance() {
        pb.setVisibility(View.VISIBLE);
        attendanceService.getMonthStudentAttendance(classId, userData.getSchoolId(), userData.getAcademicyearId(), selectedMonth).enqueue(new Callback<MonthAttendanceBean>() {
            @Override
            public void onResponse(Call<MonthAttendanceBean> call, Response<MonthAttendanceBean> response) {
                MonthAttendanceBean monthAttendanceBean = response.body();
                if (monthAttendanceBean != null) {

                    if (monthAttendanceBean.rcode == Constants.Rcode.OK) {
                        students.clear();

                        for (MonthAttendanceModel studentdata : monthAttendanceBean.data) {
                            students.add(studentdata);
                        }
                        adapter.notifyDataSetChanged();

                    } else if (monthAttendanceBean.rcode == Constants.Rcode.NORECORDS) {
                        //Toast.makeText(context, "No Student Record Found", Toast.LENGTH_SHORT).show();
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record Found");
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthAttendanceBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Month wise Attendance could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

}