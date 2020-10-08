package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.DayWiseTeacherAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayWiseTeacherAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    private AttendanceService attendanceService;
    public Context context;
    UserModel userData;
    private List<TeacherAttendanceModel> dayWiseTeacherAttendanceModels;
    private DayWiseTeacherAttendanceAdapter adapter;
    private RecyclerView listContainer;
    private ProgressDialog p_dialog;
    private ProgressBar pb;
    private TextView txtDatepickerSpinner;
    Calendar calendar;
    private String startDate;
    // RelativeLayout dateRow;
    SimpleDateFormat df, df2;
    private int year_fromDate, month_fromDate, day_fromDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    long today;
    TextView noRecordMsg, txtPresent, txtAbsent, txtHalfDay, datepicker;
    LinearLayout noRecord;
    int totalAbsent, totalPresent, totalHoloday;
    ConstraintLayout dateRow;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_daywise_attendance);
        userData = UserPreference.getInstance(context).getUserData();
        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

        calendar = Calendar.getInstance(TimeZone.getDefault());

        df2 = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        df = new SimpleDateFormat("MM/dd/yyyy");
        calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTimeInMillis();
        startDate = df.format(calendar.getTime());

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Day Wise Report", "");
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Day Wise Teacher Attendance");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        listContainer = findViewById(R.id.reyclerview_monthwise_collection_list);
        txtDatepickerSpinner = findViewById(R.id.datepickerSpinner);
        //  dateRow = findViewById(R.id.dateRow);
        datepicker = findViewById(R.id.datepickerSpinner);

        pb = findViewById(R.id.progressBar);

        txtPresent = findViewById(R.id.totalPresent);
        txtAbsent = findViewById(R.id.totalAbsent);
        txtHalfDay = findViewById(R.id.totalHalfDay);

        txtDatepickerSpinner.setText(df2.format(calendar.getTime()));
        // dateRow.setOnClickListener(this);
        datepicker.setOnClickListener(this);

        dayWiseTeacherAttendanceModels = new ArrayList<>();
        adapter = new DayWiseTeacherAttendanceAdapter(this, dayWiseTeacherAttendanceModels);
        listContainer.setAdapter(adapter);
        listContainer.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // case R.id.dateRow:

            case R.id.datepickerSpinner:
                DatePickerDialog fromDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        year_fromDate = year;
                        month_fromDate = month;
                        day_fromDate = dayOfMonth;

                        calendar.set(year, month, dayOfMonth);
                        startDate = df.format(calendar.getTime());
                        txtDatepickerSpinner.setText(df2.format(calendar.getTime()));
                        loadData();
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                fromDateDialog.getDatePicker().setMaxDate(today);
                fromDateDialog.show();
                break;

        }
    }

    private void loadData() {
        pb.setVisibility(View.VISIBLE);
        attendanceService.getTeacherAttendance(userData.getSchoolId(), userData.getAcademicyearId(), startDate).enqueue(new Callback<TeacherAttendanceBean>() {

            @Override
            public void onResponse(Call<TeacherAttendanceBean> call, Response<TeacherAttendanceBean> response) {
                TeacherAttendanceBean attendanceBean = response.body();
                dayWiseTeacherAttendanceModels.clear();

                totalAbsent = 0;
                totalHoloday = 0;
                totalPresent = 0;

                if (attendanceBean != null) {
                    if (attendanceBean.rcode == Constants.Rcode.OK) {

                        for (TeacherAttendanceModel teacherdata : attendanceBean.data) {
                            dayWiseTeacherAttendanceModels.add(teacherdata);

                            if (teacherdata.Attendance != null) {

                                if (Constants.Attendance.ABSENT == teacherdata.Attendance) {
                                    ++totalAbsent;
                                } else if (Constants.Attendance.PRESENT == teacherdata.Attendance) {
                                    ++totalPresent;
                                } else if (Constants.Attendance.HALFDAY == teacherdata.Attendance) {
                                    ++totalHoloday;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        txtAbsent.setText("Absent:" + " " + String.valueOf(totalAbsent));
                        txtPresent.setText("Present:" + " " + String.valueOf(totalPresent));
                        txtHalfDay.setText("HalfDay:" + " " + String.valueOf(totalHoloday));

                    } else if (attendanceBean.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No  Record Found", Toast.LENGTH_SHORT).show();
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record Found");
                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TeacherAttendanceBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Date wise Attendance could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
