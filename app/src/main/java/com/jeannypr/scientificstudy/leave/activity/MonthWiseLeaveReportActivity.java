package com.jeannypr.scientificstudy.leave.activity;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.api.AttendanceService;
import com.jeannypr.scientificstudy.Attendance.model.MonthBean;
import com.jeannypr.scientificstudy.Attendance.model.MonthModel;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Exam.adapter.MonthWiseLeaveAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityLeaveReportBinding;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveBean;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Author : Babulal
 * Date :
 * Month wise teacher report
 */

public class MonthWiseLeaveReportActivity extends AppCompatActivity {
    public Context context;
    AttendanceService attendanceService;
    LeaveService leaveService;
    UserPreference userPreference;
    UserModel userData;
    private MonthWiseLeaveAdapter adapter;
    private RecyclerView leaveContainer;
    EditText filterName;
    private ArrayList<MonthWiseLeaveModel> teachersLeaveList;
    Spinner leaveMonthSpinner, staffNameSpinner;
    DropDownAdapter monthAdapter;
    ArrayList<DropDownModel> months;
    private SimpleDateFormat df, df2;
    String monthName, teacherName;
    int monthId, teacherId;
    TextView noRecordMsg;
    LinearLayout noRecord;
    private ProgressBar pb;
    ActivityLeaveReportBinding binding;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leave_report);
        context = this;

        userPreference = UserPreference.getInstance(context);
        userData = userPreference.getUserData();
        attendanceService = new DataRepo<>(AttendanceService.class, context).getService();
        leaveService = new DataRepo<>(LeaveService.class, context).getService();
        teachersLeaveList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Month And Staff Wise Report", "");

        df = new SimpleDateFormat("MM yyyy", Locale.ENGLISH);
        df2 = new SimpleDateFormat("MMMMM yyyy", Locale.ENGLISH);

        pb = findViewById(R.id.progressBar);

        noRecordMsg = findViewById(R.id.noRecordMsg);
        noRecord = findViewById(R.id.noRecord);

        // filterName = findViewById(R.id.filterName);

      /*  staffNameSpinner = findViewById(R.id.ddlStaffList);
        DropDownModel model1 = new DropDownModel();
        model1.setId(-1);
        model1.setText("Select StaffName");
        months.add(model1);
        staffNameSpinner.setAdapter(monthAdapter);
        GetTeacherList();*/

/*
        staffNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DropDownModel selectedStaffName=monthAdapter.getItem(position);
                if(selectedStaffName !=null){

                    if(selectedStaffName.getId()!=-1){
                        teacherId=selectedStaffName.getId();
                        teacherName=selectedStaffName.getText();
                    }
                    else {
                        teacherId=0;
                        teacherName="";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                teacherId=0;
                teacherName="";
            }
        });
*/

        months = new ArrayList<>();
        monthAdapter = new DropDownAdapter(context, R.layout.row_spinner, months);


        leaveMonthSpinner = findViewById(R.id.ddlMonthList);
        DropDownModel model = new DropDownModel();
        model.setId(-1);
        model.setText("Select Month");
        months.add(model);
        leaveMonthSpinner.setAdapter(monthAdapter);
        getMonth();

        leaveMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                DropDownModel selectedMonth = monthAdapter.getItem(position);

                if (selectedMonth != null) {

                    if (selectedMonth.getId() != -1) {
                        monthId = selectedMonth.getId();
                        monthName = selectedMonth.getText();

                       /* try {
                            FilterByDate(monthName);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/

                        GetTeacherMonthLeave();

                    } else {
                        monthId = 0;
                        monthName = "";

                    }
                    //  GetTeacherMonthLeave();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                monthId = 0;
                monthName = "";
            }
        });


        leaveContainer = findViewById(R.id.leaveRequest);
        adapter = new MonthWiseLeaveAdapter(context, teachersLeaveList);
        leaveContainer.setAdapter(adapter);
        leaveContainer.setLayoutManager(new LinearLayoutManager(this));

        GetTeacherMonthLeave();

    }

    /*private void GetTeacherList() {
        teacherService.getTeachers(userData.getSchoolId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {

                TeacherBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        months.clear();

                        if (resp.data != null) {
                            List<TeacherModel> StaffList = resp.data;

                            for (TeacherModel teacherModel : StaffList) {
                                DropDownModel dropDownModel = new DropDownModel();
                                dropDownModel.setId(teacherModel.Id);
                                dropDownModel.setText(teacherModel.Name);
                                months.add(dropDownModel);
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

    }*/

    private void getMonth() {
        attendanceService.GetMonthList(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<MonthBean>() {
            @Override
            public void onResponse(Call<MonthBean> call, Response<MonthBean> response) {
                MonthBean monthBean = response.body();
                if (monthBean != null) {

                    if (monthBean.rcode == Constants.Rcode.OK) {

                        if (monthBean.data != null) {
                            //months.clear();
                            for (MonthModel monthModel : monthBean.data) {
                                DropDownModel dummyModel = new DropDownModel();
                                try {
                                    Date dateObj = new SimpleDateFormat("MMMMM yyyy").parse(monthModel.getMonthName());
                                    monthModel.setMonthName(df.format(dateObj));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                dummyModel.setId(monthModel.getMonthId());
                                dummyModel.setText(monthModel.getMonthName());
                                months.add(dummyModel);
                            }
                            monthAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "No month list found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MonthBean> call, Throwable t) {
                Toast.makeText(context, "Somthing went worng. No month list found", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void GetTeacherMonthLeave() {
        pb.setVisibility(View.VISIBLE);

        leaveService.GetMonthLeaveSummary(monthId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<MonthWiseLeaveBean>() {
            @Override
            public void onResponse(Call<MonthWiseLeaveBean> call, Response<MonthWiseLeaveBean> response) {
                MonthWiseLeaveBean resp = response.body();

                if (resp != null) {
                    teachersLeaveList.clear();
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            int size = resp.data.size();
                            if (size > 0) {

                                for (MonthWiseLeaveModel model : resp.data) {
                                    teachersLeaveList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                                noRecord.setVisibility(View.GONE);

                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record Found");

                                teachersLeaveList.clear();
                                adapter.notifyDataSetChanged();
                            }
                            //noRecord.setVisibility(View.GONE);
                        }
                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {

                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText("No record Found");
                        teachersLeaveList.clear();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "No record found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthWiseLeaveBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Leave Summary could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}