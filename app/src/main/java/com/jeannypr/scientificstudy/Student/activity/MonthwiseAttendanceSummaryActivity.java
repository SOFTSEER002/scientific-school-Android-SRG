package com.jeannypr.scientificstudy.Student.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.AttendanceBean;
import com.jeannypr.scientificstudy.Student.model.HolidayModel;
import com.jeannypr.scientificstudy.Student.model.StudentAttendnaceModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.roomorama.caldroid.CaldroidFragment;

public class MonthwiseAttendanceSummaryActivity extends AppCompatActivity {
    private Context context;
    int classId, schoolId, academicyearId, studentId, monthId, yearId;
    StudentService service;
    ColorDrawable absent, present, holiday;
    Map<Date, Drawable> textColorForDateMap;
    ProgressBar pb;
    CaldroidFragment caldroidFragment;
    int currentMonth, currentYear;
    Calendar cal;
    String studentName, className, subTitle;
    TextView txtPresent, txtAbsent, txtHoliday;
    int totalAbsent, totalPresent, totalHoloday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        classId = intent.getIntExtra("classId", 0);
        schoolId = intent.getIntExtra("schoolId", 0);
        academicyearId = intent.getIntExtra("academicyearId", 0);
        studentId = intent.getIntExtra("studentId", 0);

        cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        currentYear = cal.get(Calendar.YEAR);

        if (intent.hasExtra("monthId")) {
            monthId = intent.getIntExtra("monthId", 0);
        }
        if (intent.hasExtra("studentName")) {
            studentName = intent.getStringExtra("studentName");
        }
        if (intent.hasExtra("className")) {
            className = intent.getStringExtra("className");
        }
        if (intent.hasExtra("year")) {
            yearId = intent.getIntExtra("year", 0);
        }

        context = this;
        setContentView(R.layout.activity_monthwise_attendance_summary);
        pb = findViewById(R.id.progressbar);

        txtPresent = findViewById(R.id.txtPresent);
        txtAbsent = findViewById(R.id.txtAbsent);
        txtHoliday = findViewById(R.id.txtHoliday);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        subTitle = "";
        if (className != null && studentName != null) {
            subTitle = studentName + " (" + className + ")";
        }

        Utility.SetToolbar(context, "Monthly Attendance", subTitle);

        textColorForDateMap = new HashMap<>();
        absent = new ColorDrawable(getResources().getColor(android.R.color.holo_red_light));
        present = new ColorDrawable(getResources().getColor(android.R.color.holo_green_light));
        holiday = new ColorDrawable(getResources().getColor(android.R.color.holo_purple));

        caldroidFragment = new CaldroidFragment();

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                super.onCaldroidViewCreated();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);
                if (month <= currentMonth) {
                    loadData(month);

                } else if (year < currentYear ) {
                    loadData(month);
                }
                else {
                    txtAbsent.setText("");
                    txtPresent.setText("");
                    txtHoliday.setText("");

                }
            }
        });
        Bundle args = new Bundle();
        if (monthId != 0 && yearId!=0) {
            args.putInt(CaldroidFragment.MONTH, monthId);
            args.putInt(CaldroidFragment.YEAR,yearId);
        } else {
            args.putInt(CaldroidFragment.MONTH, currentMonth);
            args.putInt(CaldroidFragment.YEAR, currentYear);
        }
       // args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        caldroidFragment.setArguments(args);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();

        service = new DataRepo<>(StudentService.class, context).getService();
        //  loadData(monthId);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData(int monthId) {
        pb.setVisibility(View.VISIBLE);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        service.GetMonthlyAttendanceReport(monthId, classId, studentId, schoolId, academicyearId).enqueue(new Callback<AttendanceBean>() {
            @Override
            public void onResponse(Call<AttendanceBean> call, Response<AttendanceBean> response) {
                AttendanceBean resp = response.body();
                textColorForDateMap.clear();

                totalAbsent = 0;
                totalHoloday = 0;
                totalPresent = 0;

                if (resp != null && resp.rcode == Constants.Rcode.OK) {
                    for (StudentAttendnaceModel attendance : resp.data.Attendances) {
                        try {
                            if (attendance.IsPresent == true) {
                                ++totalPresent;
                            } else if (attendance.IsPresent == false) {
                                ++totalAbsent;
                            }

                            Date date = sdf.parse(attendance.DateStr);
                            textColorForDateMap.put(date, attendance.IsPresent ? present : absent);

                        } catch (ParseException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }

                    txtAbsent.setText(String.valueOf(totalAbsent));
                    txtPresent.setText(String.valueOf(totalPresent));

                    for (HolidayModel hol : resp.data.Holidays) {
                        try {
                            ++totalHoloday;
                            Date date = sdf.parse(hol.DateStr);
                            textColorForDateMap.put(date, holiday);

                        } catch (ParseException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    txtHoliday.setText(String.valueOf(totalHoloday));

                    caldroidFragment.setBackgroundDrawableForDates(textColorForDateMap);
                }
                caldroidFragment.refreshView();
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<AttendanceBean> call, Throwable t) {
                pb.setVisibility(View.INVISIBLE);
                caldroidFragment.refreshView();
            }
        });
    }
}