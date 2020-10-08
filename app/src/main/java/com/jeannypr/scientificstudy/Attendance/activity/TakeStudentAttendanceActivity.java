package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Attendance.adapter.TakeStudentAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceJsonModel;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceModel;
import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceSaveModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Holiday.model.HolidayBean;
import com.jeannypr.scientificstudy.Holiday.model.HolidayModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

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

public class TakeStudentAttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ProgressBar p_bar;
    private int classId;
    List<StudentAttendanceModel> students;
    List<HolidayModel> holidays;
    TakeStudentAttendanceAdapter adapter;
    UserModel userData;
    AttendanceService attendanceService;
    TextView selectedDate;
    Calendar calendar;
    //    DateFormat df;
    RelativeLayout totalHeader, rootLayout;
    TextView tvTotalPresent;
    TextView tvTotalAbsent;
    int totalPresentStudent;
    int totalAbsentStudent;
    FloatingActionButton btnSave;
    long today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_take_student_attendance);
        super.onCreate(savedInstanceState);
        context = this;

        classId = getIntent().getIntExtra("ClassId", -1);
        String className = getIntent().getStringExtra("ClassName");

        calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTimeInMillis();

        p_bar = findViewById(R.id.progressBar);
        totalHeader = findViewById(R.id.totalHeader);
        tvTotalPresent = findViewById(R.id.totalPresent);
        tvTotalAbsent = findViewById(R.id.totalAbsent);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        rootLayout = findViewById(R.id.attendanceModuleHome);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(className);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        selectedDate = findViewById(R.id.selectedDate);
//        df = new SimpleDateFormat(Constants.DATE_FORMAT_DMY2);
        selectedDate.setText(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_DMY2));

        if (classId == -1) {
            Toast.makeText(this, R.string.selectClassMsg, Toast.LENGTH_SHORT).show();
        } else {

            userData = UserPreference.getInstance(context).getUserData();
            ListView studentList = findViewById(R.id.student_attendance_list);

            holidays = new ArrayList<>();
            students = new ArrayList<>();
            adapter = new TakeStudentAttendanceAdapter(context, students, new TakeStudentAttendanceAdapter.AdapterInterface() {
                @Override
                public void attendanceChanged(int totalPresent, int totalAbsent) {
                    totalAbsentStudent = totalAbsent;
                    totalPresentStudent = totalPresent;
                    tvTotalAbsent.setText(String.valueOf(totalAbsentStudent));
                    tvTotalPresent.setText(String.valueOf(totalPresentStudent));
                }
            });

            studentList.setAdapter(adapter);
            attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            loadData(day, month, year);
            getHolidays();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.allPresent:
                for (StudentAttendanceModel student : students) {
                    student.setPresent(true);
                }
                totalAbsentStudent = 0;
                totalPresentStudent = students.size();
                tvTotalPresent.setText(String.valueOf(totalPresentStudent));
                tvTotalAbsent.setText(String.valueOf(totalAbsentStudent));
                adapter.notifyDataSetChanged();
                Toast.makeText(TakeStudentAttendanceActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.allAbsent:
                for (StudentAttendanceModel student : students) {
                    //   student.IsPresent = false;
                    student.setPresent(false);
                }
                totalAbsentStudent = students.size();
                totalPresentStudent = 0;
                tvTotalPresent.setText(String.valueOf(totalPresentStudent));
                tvTotalAbsent.setText(String.valueOf(totalAbsentStudent));
                adapter.notifyDataSetChanged();
                Toast.makeText(TakeStudentAttendanceActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearAll:
                for (StudentAttendanceModel student : students) {
                    //student.IsPresent = null;
                    student.setPresent(null);
                }
                totalAbsentStudent = 0;
                totalPresentStudent = 0;
                tvTotalPresent.setText(String.valueOf(totalPresentStudent));
                tvTotalAbsent.setText(String.valueOf(totalAbsentStudent));
                adapter.notifyDataSetChanged();
                Toast.makeText(TakeStudentAttendanceActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.dateText:
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
//                        selectedDate.setText(df.format(calendar.getTime()));
                        selectedDate.setText(Utility.GetFormattedDateMDY(calendar.getTime(), Constants.DATE_FORMAT_DMY2));

                        boolean isHoliday = isHoliday(calendar.getTime());
                        if (!isHoliday) loadData(dayOfMonth, month, year);
                        else {
                            students.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(today);
                dialog.show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isHoliday(Date selectedTime) {
        for (HolidayModel holiday : holidays) {
            SimpleDateFormat dmyFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DMY3, Locale.getDefault());
            String selectedDate = dmyFormat.format(selectedTime);
            if (holiday.Date.equals(selectedDate)) {
                Toast.makeText(context, R.string.attendanceCantTaken, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void getHolidays() {
        showLoader();
        attendanceService.getHolidays(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<HolidayBean>() {
            @Override
            public void onResponse(Call<HolidayBean> call, Response<HolidayBean> response) {
                HolidayBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            for (HolidayModel datum : resp.data.holidays) {
                                holidays.add(datum);
                            }
                        }
                    }
                } else Log.e("Failure: ", "No resps");
                hideLoader();
            }

            @Override
            public void onFailure(Call<HolidayBean> call, Throwable t) {
                Log.e("Failure: ", t.getMessage());
                hideLoader();
            }
        });
    }

    private void loadData(int day, int month, int year) {
        showLoader();
        attendanceService.getStudentAttendance(classId, userData.getSchoolId(), userData.getAcademicyearId(), day, (month + 1), year).enqueue(new Callback<StudentAttendanceBean>() {
            @Override
            public void onResponse(Call<StudentAttendanceBean> call, Response<StudentAttendanceBean> response) {
                StudentAttendanceBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        students.clear();
                        totalAbsentStudent = 0;
                        totalPresentStudent = 0;

                        for (StudentAttendanceModel student : resp.data) {
                            if (student.getPresent() != null) {
                                if (student.getPresent()) {
                                    ++totalPresentStudent;
                                } else {
                                    ++totalAbsentStudent;
                                }
                            }

                            students.add(student);
                        }
                        tvTotalAbsent.setText(String.valueOf(totalAbsentStudent));
                        tvTotalPresent.setText(String.valueOf(totalPresentStudent));
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, R.string.studentListNotFound, Toast.LENGTH_LONG).show();
                    }
                }
                hideLoader();

            }

            @Override
            public void onFailure(Call<StudentAttendanceBean> call, Throwable t) {
                Log.d("studentList", "server call error");
                hideLoader();
                Toast.makeText(context, R.string.studentListNotFound, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showLoader() {
        p_bar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        p_bar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSave:
                if (totalAbsentStudent + totalPresentStudent != students.size()) {
                    Toast.makeText(context, "Please mark attendance for all students!", Toast.LENGTH_LONG).show();

                } else {
                    ArrayList<StudentAttendanceJsonModel> data = new ArrayList<>();
                    for (StudentAttendanceModel student : students) {
                        if (student.getPresent() != null) {

                            StudentAttendanceJsonModel item = new StudentAttendanceJsonModel();
                      /*      item.SId = student.StudentId;
                            item.IsPresent = student.IsPresent;*/
                            item.setSId(student.getStudentId());
                            item.setPresent(student.getPresent());
                            data.add(item);

                        } else {
                            Toast.makeText(context, "Please mark attendance for all students!", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    StudentAttendanceSaveModel model = new StudentAttendanceSaveModel();
               /*     model.ClassId = classId;
                    model.SchoolId = userdata.SchoolId;
                    model.AcademicyearId = userdata.AcademicyearId;
                    model.CreatedBy = userdata.UserId;
                    model.Day = calendar.get(Calendar.DAY_OF_MONTH);
                    model.Month = calendar.get(Calendar.MONTH) + 1;
                    model.Year = calendar.get(Calendar.YEAR);
                    model.AttendanceArr = new Gson().toJson(data);
                    model.Note = "";*/

                    model.setClassId(classId);
                    model.setSchoolId(userData.getSchoolId());
                    model.setAcademicyearId(userData.getAcademicyearId());
                    model.setCreatedBy(userData.getUserId());
                    model.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    model.setMonth(calendar.get(Calendar.MONTH) + 1);
                    // model.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                    model.setYear(calendar.get(Calendar.YEAR));
                    model.setAttendanceArr(new Gson().toJson(data));
                    model.setNote("");
                    showLoader();

                    attendanceService.saveStudentAttendance(model).enqueue(new Callback<StudentAttendanceBean>() {
                        @Override
                        public void onResponse(Call<StudentAttendanceBean> call, Response<StudentAttendanceBean> response) {
                            StudentAttendanceBean resp = response.body();

                            if (resp != null) {
                                if (resp.rcode == Constants.Rcode.OK) {
                                    Toast.makeText(context, "Attendance saved successfully.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                            }
                            hideLoader();

                        }

                        @Override
                        public void onFailure(Call<StudentAttendanceBean> call, Throwable t) {
                            Log.d("attendance", "server call error");
                            hideLoader();
                            Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                    ;
                }
                break;
        }

    }
}
