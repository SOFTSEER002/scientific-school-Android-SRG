package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.DateWiseAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateWiseAttendanceSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    public Context context;
    int classId;
    String className;
    RecyclerView listContainer;
    private ProgressBar pb;
    private TextView txtFromDate, txtTodate;
    private Calendar calendar;
    private SimpleDateFormat df;
    private String toDate, fromDate;
    private int year_fromDate, month_fromDate, day_fromDate;
    AttendanceService attendanceService;
    List<StudentAttendanceModel> students;
    UserModel userData;
    private int selectedYear, selectedMonth, selectedDay;
    private DateWiseAttendanceAdapter adapter;
    private RecyclerView datewiseAttendanceList;
    TextView total_Present;
    TextView total_Absent;
    ImageView fromDateIc;
    int totalPresentStudent;
    int totalAbsentStudent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_attendance_summary);
        context = this;

        classId = getIntent().getIntExtra("classId", -1);
        className = getIntent().getStringExtra("className");

        total_Present = findViewById(R.id.total_Present);
        total_Absent = findViewById(R.id.total_Absent);

        userData = UserPreference.getInstance(context).getUserData();
        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

        df = new SimpleDateFormat("dd-MMM-yyyy");

        calendar = Calendar.getInstance(TimeZone.getDefault());
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);

        listContainer = findViewById(R.id.reyclerview_datewise_collection_list);
        txtFromDate = findViewById(R.id.fromDateVal);
        txtFromDate.setOnClickListener(this);

        fromDateIc = findViewById(R.id.fromDateIc);
        fromDateIc.setOnClickListener(this);

        pb = findViewById(R.id.progressBar);

        txtFromDate.setText(df.format(calendar.getTime()));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Date Wise Attendance");
            getSupportActionBar().setSubtitle(className);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        datewiseAttendanceList = findViewById(R.id.datewiseAttendanceList);
        students = new ArrayList<>();
        adapter = new DateWiseAttendanceAdapter(context, students);
        datewiseAttendanceList.setAdapter(adapter);
        GetStudentList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fromDateIc:
            case R.id.fromDateVal:
                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;

                        calendar.set(year, monthOfYear, dayOfMonth);
                        fromDate = df.format(calendar.getTime());
                        txtFromDate.setText(fromDate);
                        GetStudentList();
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                fromDateDialog.show();
                break;

           /* case R.id.toDateVal_datewiseAttandance:
                DatePickerDialog toDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        toDate = df.format(calendar.getTime());
                        txtTodate.setText(toDate);

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                Date min = new Date();
                min.setDate(day_fromDate);
                min.setMonth(month_fromDate);
                min.setYear(year_fromDate);
                toDateDialog.getDatePicker().setMinDate(min.getDate());
                toDateDialog.show();
                break;*/
        }
    }

    private void GetStudentList() {
        pb.setVisibility(View.VISIBLE);

        attendanceService.getStudentAttendance(classId, userData.getSchoolId(), userData.getAcademicyearId(), selectedDay, selectedMonth, selectedYear).enqueue(new Callback<StudentAttendanceBean>() {

            @Override
            public void onResponse(Call<StudentAttendanceBean> call, Response<StudentAttendanceBean> response) {
                StudentAttendanceBean attendanceBean = response.body();
                if (attendanceBean != null) {

                    if (attendanceBean.rcode == Constants.Rcode.OK) {
                        students.clear();
                        totalAbsentStudent = 0;
                        totalPresentStudent = 0;
                        for (StudentAttendanceModel studentdata : attendanceBean.data) {
                            if (studentdata.getPresent() != null) {
                                if (studentdata.getPresent() == false) {
                                    ++totalPresentStudent;
                                } else if (studentdata.getPresent() == true) {
                                    ++totalAbsentStudent;
                                }
                            }
                            students.add(studentdata);
                        }
                        total_Present.setText(String.valueOf(totalAbsentStudent));
                        total_Absent.setText(String.valueOf(totalPresentStudent));
                        adapter.notifyDataSetChanged();

                    } else if (attendanceBean.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StudentAttendanceBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Date wise Attendance could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

}