package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.ExtarDayAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.ExtraDayReportBean;
import com.jeannypr.scientificstudy.Attendance.model.ExtraDayReportModel;
import com.jeannypr.scientificstudy.Attendance.model.MonthBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.TeacherBean;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutDoorAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    private AttendanceService attendanceService;


    private TeacherService teacherService;
    public Context context;
    UserModel userData;
    private Spinner StaffList;
    private DropDownAdapter dropDownAdapter;
    ArrayList<DropDownModel> teachers;
    DropDownModel selectedTeacher;
    private ArrayList<ExtraDayReportModel> extraDays;
    private ExtarDayAttendanceAdapter adapter;
    private RecyclerView outDoorAttendanceList;
    private ProgressBar pb;
    private TextView datepicker, noRecordMsg;
    //  Calendar calendar;
    private RelativeLayout outAttendance;
    private LinearLayout noRecord;
    SimpleDateFormat df, df_dmy;
    private int monthId;
    String monthName;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    int teacherId = 0;
    String teacherName;
    DropDownAdapter monthAdapter;
    ArrayList<DropDownModel> months;
    Spinner monthSpinner;
    private static final String TAG = ExtraDayAttendanceActivity.class.getSimpleName();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_out_door_report);

        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();
        userData = UserPreference.getInstance(context).getUserData();
        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        months = new ArrayList<>();
        monthAdapter = new DropDownAdapter(context, R.layout.row_spinner, months);
        teachers = new ArrayList<>();
        dropDownAdapter = new DropDownAdapter(OutDoorAttendanceActivity.this, R.layout.row_spinner, teachers);

        // calendar = Calendar.getInstance(TimeZone.getDefault());

        //selectedMonth = 0;
        //selectedYear = calendar.get(Calendar.YEAR);

        df = new SimpleDateFormat("MMM-yyyy");
        df_dmy = new SimpleDateFormat("dd MMM, yyyy");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "OutDoor Day Report", "");

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
                        //  adapter.getFilter().filter(String.valueOf(monthId));
                        loadData();

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

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        outDoorAttendanceList = findViewById(R.id.outDoorAttendanceList);
      /*  datepicker = findViewById(R.id.datepicker);
        datepicker.setText(df.format(calendar.getTime()));
        outAttendance = findViewById(R.id.outAttendance);
        outAttendance.setOnClickListener(this);*/

        pb = findViewById(R.id.progressBar);

        extraDays = new ArrayList<>();
        adapter = new ExtarDayAttendanceAdapter(context, extraDays);
        outDoorAttendanceList.setAdapter(adapter);
        outDoorAttendanceList.setLayoutManager(new LinearLayoutManager(this));

        StaffList = findViewById(R.id.ddlTeacherList);
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select staff");
        defaultOption.setId(-1);
        teachers.add(defaultOption);
        StaffList.setAdapter(dropDownAdapter);

        StaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedTeacher = dropDownAdapter.getItem(position);
                if (selectedTeacher != null) {
                    if (selectedTeacher.getId() == -1) {
                        teacherId = 0;
                        teacherName = "";
                    } else {
                        teacherId = selectedTeacher.getId();
                        teacherName = selectedTeacher.getText();
                        loadData();
//                        adapter.getFilter().filter(String.valueOf(teacherName));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadData();
        GetTeacherList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.outAttendance:

                DatePickerDialog dialog = new DatePickerDialog(ExtraDayAttendanceActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.YEAR, year);
                        selectedMonth = monthOfYear + 1;
                        //selectedYear = year;
                        datepicker.setText(df.format(calendar.getTime()));
                        String selectedDate = df_dmy.format(calendar.getTime());

                        adapter.getFilter().filter(String.valueOf(selectedDate));
                        // loadData();

                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                // dialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                dialog.show();
                break;*/

        }
    }

    private void GetTeacherList() {
        teacherService.getTeachers(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {

                TeacherBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        teachers.clear();

                        if (resp.data != null) {
                            List<TeacherModel> StaffList = resp.data;

                            for (TeacherModel teacherModel : StaffList) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(teacherModel.Id);
                                dropDownModel.setText(teacherModel.Name);
                                teachers.add(dropDownModel);
                            }
                            adapter.notifyDataSetChanged();

                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Student list could not be loaded .Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loadData() {
        pb.setVisibility(View.VISIBLE);
        attendanceService.getOutDoorAttendance(teacherId, monthId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ExtraDayReportBean>() {

            @Override
            public void onResponse(Call<ExtraDayReportBean> call, Response<ExtraDayReportBean> response) {
                ExtraDayReportBean extraDayReportBean = response.body();
                extraDays.clear();

                if (extraDayReportBean != null) {

                    int size = extraDayReportBean.data.size();
                    if (size == 0) {
                        noRecordMsg.setText("No record found.");
                        noRecord.setVisibility(View.VISIBLE);
                    }
                    if (extraDayReportBean.rcode == Constants.Rcode.OK) {

                        for (ExtraDayReportModel teacherdata : extraDayReportBean.data) {
                            extraDays.add(teacherdata);
                        }
                        adapter.notifyDataSetChanged();

                    } else if (extraDayReportBean.rcode == Constants.Rcode.NORECORDS) {
                        noRecordMsg.setText("No record found.");
                        noRecord.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ExtraDayReportBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Report could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GetMonths() {
        attendanceService.GetMonthList(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<MonthBean>() {
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
                Toast.makeText(context, "Somethind went wrong. No month list found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

