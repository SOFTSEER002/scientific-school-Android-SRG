package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.MonthWiseTeacherAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.MonthBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthModel;
import com.jeannypr.scientificstudy.Attendance.model.MonthTeacherAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthTeacherAttendanceModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthWiseTeacherAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    AttendanceService attendanceService;
    public Context context;
    UserModel userdata;
    UserPreference userPreference;
    private List<MonthTeacherAttendanceModel> teachers;
    private MonthWiseTeacherAttendanceAdapter adapter;
    private int monthId;
    SimpleDateFormat dateFormat;
    // private TextView datepickerSpinner;
    Spinner monthSpinner;
    Calendar calendar;
    String monthName;
    private ProgressBar pb;
    private RecyclerView monthwiseAttendanceList;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    DropDownAdapter monthAdapter;
    ArrayList<DropDownModel> months;
    private static final String TAG = MonthWiseTeacherAttendanceActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_wise_teacher_attendance);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userdata = userPreference.getUserData();
        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Month Wise Report", "");

        pb = findViewById(R.id.progressBar);


        //   calendar = Calendar.getInstance(TimeZone.getDefault());

      /*  selectedMonth = calendar.get(Calendar.MONTH) + 1;
        selectedYear = calendar.get(Calendar.YEAR);*/

        months = new ArrayList<>();
        monthAdapter = new DropDownAdapter(context, R.layout.row_spinner, months);
        dateFormat = new SimpleDateFormat("MMM-yyyy");

        monthSpinner = findViewById(R.id.ddlMonthList);
        DropDownModel model = new DropDownModel();
        model.setId(-1);
        model.setText("Select month");
        months.add(model);
        monthSpinner.setAdapter(monthAdapter);

        GetMonths();

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DropDownModel selectedMonth = monthAdapter.getItem(position);

                if (selectedMonth != null) {
                    if (selectedMonth.getId() != -1) {
                        monthId = selectedMonth.getId();
                        monthName = selectedMonth.getText();
                        getTeacherAttendance();

                    } else {
                        monthId = 0;
                        monthName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                monthId = 0;
                monthName = "";
            }
        });

        monthwiseAttendanceList = findViewById(R.id.monthwiseAttendanceList);
        teachers = new ArrayList<>();
        adapter = new MonthWiseTeacherAttendanceAdapter(context, teachers, new MonthWiseTeacherAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MonthTeacherAttendanceModel model) {
              /*  Intent intent = new Intent(context, MonthwiseAttendanceSummaryActivity.class);

                intent.putExtra("teacherId", model.TeacherId);
                intent.putExtra("month", selectedMonth);
                intent.putExtra("halfDay", model.HalfDay);
                intent.putExtra("absent", model.Absent);
                intent.putExtra("present", model.Present);
                intent.putExtra("teacherName", model.TeacherName);
                startActivity(intent);*/
            }
        });
        monthwiseAttendanceList.setAdapter(adapter);
        //   getTeacherAttendance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void GetMonths() {
        attendanceService.GetMonthList(userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<MonthBean>() {
            @Override
            public void onResponse(Call<MonthBean> call, Response<MonthBean> response) {
                MonthBean bean = response.body();
                if (bean != null) {

                    if (bean.rcode == Constants.Rcode.OK) {
                        if (bean.data != null)
                            for (MonthModel datum : bean.data) {

                                DropDownModel dummyModel = new DropDownModel();
                                dummyModel.setId(datum.getMonthId());
                                dummyModel.setText(datum.getMonthName());
                                months.add(dummyModel);
                            }
                        monthAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "No month list found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MonthBean> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(context, "Somethind went wrong.No month list found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

          /*  case R.id.monthSpinner:
                DatePickerDialog dialog = new DatePickerDialog(MonthWiseTeacherAttendanceActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        datepickerSpinner.setText(dateFormat.format(calendar.getTime()));
                        getTeacherAttendance();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
//                dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                //TODO : set current month and year as max date
                dialog.show();
                break;*/

        }
    }

    private void getTeacherAttendance() {
        pb.setVisibility(View.VISIBLE);

        attendanceService.getMonthTeacherAttendance(monthId, userdata.getSchoolId(), userdata.getAcademicyearId()).enqueue(new Callback<MonthTeacherAttendanceBean>() {
            @Override
            public void onResponse(Call<MonthTeacherAttendanceBean> call, Response<MonthTeacherAttendanceBean> response) {
                MonthTeacherAttendanceBean teacherData = response.body();

                if (teacherData != null) {
                    if (teacherData.rcode == Constants.Rcode.OK) {
                        teachers.clear();

                        for (MonthTeacherAttendanceModel teacherList : teacherData.data) {
                            teachers.add(teacherList);

                        }

                        adapter.notifyDataSetChanged();

                    } else if (teacherData.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "No record found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthTeacherAttendanceBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Month wise teacher attendance could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
