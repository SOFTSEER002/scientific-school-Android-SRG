package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Attendance.adapter.TeacherAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceJsonModel;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceModel;
import com.jeannypr.scientificstudy.Attendance.model.TeacherAttendanceSaveModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import androidx.databinding.DataBindingUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.jeannypr.scientificstudy.databinding.ActivityTeacherAttendanceBinding;

public class TeacherAttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ProgressBar p_bar;
    ImageView popupMenu;
    List<TeacherAttendanceModel> teachers;
    TeacherAttendanceAdapter adapter;
    UserModel userdata;
    AttendanceService attendanceService;
    TextView selectedDate;
    Calendar calendar;
    DateFormat df;
    DateFormat sf;
    RelativeLayout totalHeader;
    TextView tvTotalPresent;
    TextView tvTotalAbsent;
    int totalPresentTeacher;
    int totalAbsentTeacher;
    int totalHalfDayTeacher;
    FloatingActionButton btnSave;
    RecyclerView mTeacherRecycler;
    long today;

    private ActivityTeacherAttendanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_attendance);
        //setContentView(R.layout.activity_teacher_attendance);
        context = this;
//        popupMenu = findViewById(R.id.popupMenu);
//        popupMenu.setOnClickListener(this);

        p_bar = findViewById(R.id.progressBar);
        totalHeader = findViewById(R.id.totalHeader);
        tvTotalPresent = findViewById(R.id.totalPresent);
        tvTotalAbsent = findViewById(R.id.totalAbsent);
        btnSave = (FloatingActionButton) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTimeInMillis();

//        TextView classTextView= findViewById(R.id.className);
//        classTextView.setText("Teacher Attendance");
//        ImageView datepicker = findViewById(R.id.datepicker);
//        datepicker.setOnClickListener(this);

        selectedDate = findViewById(R.id.selectedDate);
        selectedDate.setOnClickListener(this);
        df = new SimpleDateFormat("dd-MM-yyyy");
        selectedDate.setText(df.format(calendar.getTime()));

        userdata = UserPreference.getInstance(context).getUserData();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Teacher Attendance");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        teachers = new ArrayList<TeacherAttendanceModel>();


        adapter = new TeacherAttendanceAdapter(TeacherAttendanceActivity.this, teachers, new TeacherAttendanceAdapter.OnAttendanceChangeListener() {
            @Override
            public void attendanceChanged(int totalPresent, int totalAbsent, int totalHalfDay) {
                totalAbsentTeacher = totalAbsent;
                totalPresentTeacher = totalPresent;
                totalHalfDayTeacher = totalHalfDay;
                tvTotalAbsent.setText(String.valueOf(totalAbsentTeacher));
                tvTotalPresent.setText(String.valueOf(totalPresentTeacher));
            }
        });


        mTeacherRecycler = findViewById(R.id.teacher_attendance_list);

        mTeacherRecycler.setAdapter(adapter);
        mTeacherRecycler.setLayoutManager(new LinearLayoutManager(context));

        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

        sf = new SimpleDateFormat("MM/dd/yyyy");

        String date = sf.format(calendar.getTime());

//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);

        loadData(date);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void loadData(String date) {
        showLoader("Getting teacher list. Please wait...");

        attendanceService.getTeacherAttendance(userdata.getSchoolId(), userdata.getAcademicyearId(), date).enqueue(new Callback<TeacherAttendanceBean>() {
            @Override
            public void onResponse(Call<TeacherAttendanceBean> call, Response<TeacherAttendanceBean> response) {
                TeacherAttendanceBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        teachers.clear();
                        totalAbsentTeacher = 0;
                        totalPresentTeacher = 0;
                        totalHalfDayTeacher = 0;

                        for (TeacherAttendanceModel teacher : resp.data) {
                            if (teacher.Attendance != null) {
                                if (teacher.Attendance == 1) {
                                    ++totalPresentTeacher;

                                } else if (teacher.Attendance == 0) {
                                    ++totalAbsentTeacher;
                                } else {
                                    ++totalHalfDayTeacher;
                                }
                            }

                            teachers.add(teacher);
                        }
                        tvTotalAbsent.setText(String.valueOf(totalAbsentTeacher));
                        tvTotalPresent.setText(String.valueOf(totalPresentTeacher));

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Teacher list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                hideLoader();

            }

            @Override
            public void onFailure(Call<TeacherAttendanceBean> call, Throwable t) {
                Log.d("teacherList", "server call error");
                hideLoader();
                Toast.makeText(context, "Teacher list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void showLoader(String msg) {
        p_bar.setVisibility(View.VISIBLE);

    }

    private void hideLoader() {
        p_bar.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.allPresent:
                for (TeacherAttendanceModel teacher : teachers) {
                    teacher.Attendance = 1;
                }
                totalAbsentTeacher = 0;
                totalHalfDayTeacher = 0;
                totalPresentTeacher = teachers.size();
                tvTotalPresent.setText(String.valueOf(totalPresentTeacher));
                tvTotalAbsent.setText(String.valueOf(totalAbsentTeacher));
                adapter.notifyDataSetChanged();
                break;
            case R.id.allAbsent:
                for (TeacherAttendanceModel teacher : teachers) {
                    teacher.Attendance = 0;
                    teacher.IsOutdoor = false;
                    teacher.IsExtra = false;
                    teacher.Notes = "";
                }
                totalAbsentTeacher = teachers.size();
                totalPresentTeacher = 0;
                totalHalfDayTeacher = 0;
                tvTotalPresent.setText(String.valueOf(totalPresentTeacher));
                tvTotalAbsent.setText(String.valueOf(totalAbsentTeacher));
                adapter.notifyDataSetChanged();
                break;
            case R.id.clearAll:
                for (TeacherAttendanceModel teacher : teachers) {
                    teacher.Attendance = null;
                    teacher.IsOutdoor = false;
                    teacher.IsExtra = false;
                    teacher.Notes = "";
                }
                totalAbsentTeacher = 0;
                totalPresentTeacher = 0;
                totalHalfDayTeacher = 0;
                tvTotalPresent.setText(String.valueOf(totalPresentTeacher));
                tvTotalAbsent.setText(String.valueOf(totalAbsentTeacher));
                adapter.notifyDataSetChanged();
                break;
            case R.id.dateText:
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        selectedDate.setText(df.format(calendar.getTime()));
                        loadData(sf.format(calendar.getTime()));
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(today);

                dialog.show();
                break;
        }
        //Toast.makeText(TeacherAttendanceActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectedDate:
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        selectedDate.setText(df.format(calendar.getTime()));
                        loadData(sf.format(calendar.getTime()));
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(today);

                dialog.show();
                break;
            case R.id.btnSave:
                ArrayList<TeacherAttendanceJsonModel> data = new ArrayList<>();
                for (TeacherAttendanceModel teacher : teachers) {
                    if (teacher.Attendance != null) {
                        TeacherAttendanceJsonModel item = new TeacherAttendanceJsonModel();
                        item.Id = teacher.Id;
                        item.Attendance = teacher.Attendance;
                        item.IsExtra = teacher.IsExtra;
                        item.OutDoor = teacher.IsOutdoor;
                        item.Notes = teacher.Notes;
                        data.add(item);
                    }
                }
                TeacherAttendanceSaveModel model = new TeacherAttendanceSaveModel();
                // model.ClassId = classId;
                model.SchoolId = userdata.getSchoolId();
                model.AcademicyearId = userdata.getAcademicyearId();
                model.CreatedBy = userdata.getUserId();
                model.Day = calendar.get(Calendar.DAY_OF_MONTH);
                model.Month = calendar.get(Calendar.MONTH) + 1;
                model.Year = calendar.get(Calendar.YEAR);
                model.AttendanceArr = new Gson().toJson(data);

                showLoader("Saving attendance. Please wait...");
                attendanceService.saveTeacherAttendance(model).enqueue(new Callback<TeacherAttendanceBean>() {
                    @Override
                    public void onResponse(Call<TeacherAttendanceBean> call, Response<TeacherAttendanceBean> response) {
                        TeacherAttendanceBean resp = response.body();

                        if (resp.rcode == Constants.Rcode.OK) {
                            Toast.makeText(context, "Attendance saved successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                        }
                        hideLoader();

                    }

                    @Override
                    public void onFailure(Call<TeacherAttendanceBean> call, Throwable t) {
                        Log.d("attendance", "server call error");
                        hideLoader();
                        Toast.makeText(context, "Attendance could not be saved. Try again!", Toast.LENGTH_LONG).show();
                    }
                });
                ;

                break;
        }

    }
}
