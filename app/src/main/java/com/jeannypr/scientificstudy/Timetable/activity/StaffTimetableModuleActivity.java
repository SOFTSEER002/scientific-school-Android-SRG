package com.jeannypr.scientificstudy.Timetable.activity;

/*
 * Author : Babulal
 * Date :30/11/2018
 * Staff Timetable Module
 */


import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jeannypr.scientificstudy.Timetable.api.TimetableService;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsBean;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityStaffTimetableModuleBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffTimetableModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TeacherService teacherService;
    TimetableService timetableService;
    UserPreference userPref;
    UserModel userData;
    private Spinner ddlStaffList, ddlShiftList;
    ArrayList<DropDownModel> teacherList, shiftList;
    private DropDownModel selectedTeacher, selectedShift;
    DropDownAdapter teacherListAdapter, shiftAdapter;
    private int teacherUserId, shiftId;
    String teacherName, shiftName, timetableOf;
    ActivityStaffTimetableModuleBinding binding;
    RelativeLayout shifTimetableSpinner;
    ProgressBar pb;
    FloatingActionButton showTimetableBtn;
    private TextView noRecordMsg;
    private LinearLayout noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_timetable_module);
        context = this;

        teacherService = new DataRepo<>(TeacherService.class, context).getService();
        timetableService = new DataRepo<>(TimetableService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        timetableOf = getIntent().getStringExtra(Constants.Timetable_INTENT);

        ddlStaffList = findViewById(R.id.ddlStaffList);
        ddlShiftList = findViewById(R.id.ddlShiftList);
        shifTimetableSpinner = findViewById(R.id.shifTimetableSpinner);
        showTimetableBtn = findViewById(R.id.showTimetableBtn);
        showTimetableBtn.setOnClickListener(this);
        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Timetable", "");


        teacherList = new ArrayList<>();
        teacherListAdapter = new DropDownAdapter(context, R.layout.row_spinner, teacherList);
        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select Teacher");
        teacherList.add(model);
        ddlStaffList.setAdapter(teacherListAdapter);
        GetTeacherList();

        ddlStaffList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedTeacher = teacherListAdapter.getItem(position);
                if (selectedTeacher != null) {
                    if (selectedTeacher.getId() != 0) {
                        teacherUserId = selectedTeacher.getId();
                        teacherName = selectedTeacher.getText();
                        chooseShift();
                    } else {
                        teacherUserId = 0;
                        teacherName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        shiftList = new ArrayList<>();
        shiftAdapter = new DropDownAdapter(context, R.layout.row_spinner, shiftList);

        DropDownModel model1 = new DropDownModel();
        model1.setId(0);
        model1.setText("Select Shift");
        shiftList.add(model1);
        ddlShiftList.setAdapter(shiftAdapter);
        GetShiftList();

        ddlShiftList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedShift = shiftAdapter.getItem(position);

                if (selectedShift != null) {

                    if (selectedShift.getId() != 0) {
                        shiftId = selectedShift.getId();
                        shiftName = selectedShift.getText();

                        if (selectedTeacher.getId() != 0) {
                            chooseShift();
                        }

                    } else {
                        shiftId = 0;
                        shiftName = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                shiftId = 0;
                shiftName = "";
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void GetTeacherList() {
        teacherService.getTeachers(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {

                TeacherBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<TeacherModel> allTeachers = resp.data;

                        for (TeacherModel teacher : allTeachers) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(teacher.UserId);
                            dropDownModel.setText(teacher.Name);
                            teacherList.add(dropDownModel);
                        }
                        teacherListAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Teachers could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chooseShift() {

        Intent timetableIntent = new Intent(context, TimetableModuleActivity.class);
        timetableIntent.putExtra("teacherId", teacherUserId);
        timetableIntent.putExtra("teacherName", teacherName);
        timetableIntent.putExtra("shiftId", shiftId);
        timetableIntent.putExtra("shiftName", shiftName);
        timetableIntent.putExtra("timetableOf", timetableOf);
        startActivity(timetableIntent);
    }

    private void GetShiftList() {
        pb.setVisibility(View.VISIBLE);
        timetableService.GetSchoolShifts(userData.getSchoolId()).enqueue(new Callback<SchoolShiftsBean>() {
            @Override
            public void onResponse(Call<SchoolShiftsBean> call, Response<SchoolShiftsBean> response) {
                SchoolShiftsBean resp = response.body();

                if (resp != null) {
                    int size = resp.data.size();
                    if (size > 1) {
                        shifTimetableSpinner.setVisibility(View.VISIBLE);
                    } else {
                        if (size == 0) {
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No record found.");

                        } else {
                            shiftId = resp.data.get(0).Id;
                            shiftName = resp.data.get(0).ShiftName;
                        }
                    }

                    shiftList.clear();
                    if (resp.rcode == Constants.Rcode.OK) {
                        List<SchoolShiftsModel> allShift = resp.data;

                        for (SchoolShiftsModel shift : allShift) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(shift.Id);
                            dropDownModel.setText(shift.ShiftName);
                            shiftList.add(dropDownModel);
                        }
                        shiftAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Shift could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SchoolShiftsBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showTimetableBtn:
            /*    if ((selectedTeacher.getId() != 0) && (selectedShift.getId() != 0)) {
                    chooseShift();
                } else {
                    Toast.makeText(context, "Please select teacher", Toast.LENGTH_SHORT).show();
                }*/
                if ((teacherUserId != 0) && (shiftId != 0)) {
                    chooseShift();
                } else {
                    Toast.makeText(context, "Please select teacher", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

