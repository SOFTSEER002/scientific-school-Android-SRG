package com.jeannypr.scientificstudy.Attendance.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Attendance.adapter.StaffWiseAttendanceAdapter;
import com.jeannypr.scientificstudy.Attendance.model.StaffWiseAttendanceBean;
import com.jeannypr.scientificstudy.Attendance.model.StaffWiseAttendanceModel;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffWiseAttenenceActivity extends AppCompatActivity {
    int classId, teacherId;
    String className, teacherName;
    public Context context;
    TeacherService teacherService;
    private Spinner StaffList;
    private DropDownAdapter adapter;
    DropDownModel selectedTeacher;
    StaffWiseAttendanceModel staffWiseAttendanceModel;
    ArrayList<DropDownModel> teachers;
    ArrayList<StaffWiseAttendanceModel> attendance;
    StaffWiseAttendanceAdapter attendanceAdapter;
    UserPreference userPreference;
    UserModel userModel;
    RecyclerView attendanceList;
    private ProgressBar pb;
    LinearLayout noRecord;
    TextView noRecordMsg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_wise_attendance);
        context = this;

        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        userPreference = UserPreference.getInstance(context);
        userModel = userPreference.getUserData();

        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
       /* findViewById(R.id.divider2).setVisibility(View.VISIBLE);
        findViewById(R.id.totalHalfdayLbl).setVisibility(View.VISIBLE);*/

        attendance = new ArrayList<>();
        attendanceAdapter = new StaffWiseAttendanceAdapter(context, attendance, new StaffWiseAttendanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StaffWiseAttendanceModel model) {

            }
        });
        attendanceList = findViewById(R.id.attendanceList);
        attendanceList.setAdapter(attendanceAdapter);
        attendanceList.setLayoutManager(new LinearLayoutManager(this));
        //  GetTeacherAttendance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Staff Wise Report", "");

       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Staff Wise Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        teachers = new ArrayList<>();
        adapter = new DropDownAdapter(StaffWiseAttenenceActivity.this, R.layout.row_spinner, teachers);

        StaffList = findViewById(R.id.ddlStudentList);
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText("Select staff");
        defaultOption.setId(-1);
        teachers.add(defaultOption);

        StaffList.setAdapter(adapter);
        StaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedTeacher = adapter.getItem(position);

                if (selectedTeacher != null) {
                    if (selectedTeacher.getId() == -1) {
//                        selectedTeacher = null;
                        teacherId = -1;
                        teacherName = "";

                    } else {
                        teacherId = selectedTeacher.getId();
                        teacherName = selectedTeacher.getText();
                        GetTeacherAttendance();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetTeacherList();

    }


    private void GetTeacherList() {
        teacherService.getTeachers(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {

                TeacherBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
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

    private void GetTeacherAttendance() {
        pb.setVisibility(View.VISIBLE);
        teacherService.getstaffAttendance(teacherId, userModel.getSchoolId(), userModel.getAcademicyearId())
                .enqueue(new Callback<StaffWiseAttendanceBean>() {

                    @Override
                    public void onResponse(Call<StaffWiseAttendanceBean> call, Response<StaffWiseAttendanceBean> response) {
                        StaffWiseAttendanceBean resp = response.body();
                        if (resp != null) {
                            attendance.clear();

                            if (resp.rcode == Constants.Rcode.OK) {
                                if (resp.data != null) {

                                    for (StaffWiseAttendanceModel datum : resp.data) {
                                        attendance.add(datum);
                                    }
                                    attendanceAdapter.notifyDataSetChanged();
                                }

                            } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                                //Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                                noRecord.setVisibility(View.VISIBLE);
                                noRecordMsg.setText("No record found.");

                            } else {
                                Toast.makeText(context, "teacher list coould not be loaded .Try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                        }
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<StaffWiseAttendanceBean> call, Throwable t) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(context, "Date wise Attendance could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }

                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
