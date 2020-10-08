package com.jeannypr.scientificstudy.Timetable.activity;

/*
 * Author : Babulal
 * Date :7/12/2018
 * Staff Timetable Module
 */

import android.content.Context;
import android.content.Intent;
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
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Timetable.api.TimetableService;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsBean;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTimetableModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    BaseService baseService;
    StudentService studentService;
    TimetableService timetableService;
    UserPreference userPref;
    UserModel userData;
    private Spinner shiftList, classList;
    ArrayList<DropDownModel> shifts, classes;
    DropDownModel selectedShift, selectedClass;
    DropDownAdapter shiftAdapter, classAdapter;
    private int shiftId, classId;
    String shiftName, className, timetableOf;
    ProgressBar pb;
    RelativeLayout shifTimetableSpinner;
    FloatingActionButton showTimetableBtn;
    private TextView noRecordMsg;
    private LinearLayout noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_timetable_module);
        context = this;

        baseService = new DataRepo<>(BaseService.class, context).getService();
        studentService = new DataRepo<>(StudentService.class, context).getService();
        timetableService = new DataRepo<>(TimetableService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        timetableOf = getIntent().getStringExtra(Constants.Timetable_INTENT);

        classList = findViewById(R.id.ddlStaffList);
        shiftList = findViewById(R.id.ddlShiftList);
        shifTimetableSpinner = findViewById(R.id.shifTimetableSpinner);
        pb = findViewById(R.id.progressBar);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        showTimetableBtn = findViewById(R.id.showTimetableBtn);
        showTimetableBtn.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Timetable", "");


        classes = new ArrayList<>();
        classAdapter = new DropDownAdapter(context, R.layout.row_spinner, classes);
        DropDownModel model = new DropDownModel();
        model.setId(0);
        model.setText("Select class");
        classes.add(model);
        classList.setAdapter(classAdapter);

        GetClassesList();

        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedClass = classAdapter.getItem(position);
                if (selectedClass != null) {
                    if (selectedClass.getId() != 0) {
                        classId = selectedClass.getId();
                        className = selectedClass.getText();
                        chooseShift();
                    } else {
                        classId = 0;
                        className = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        shifts = new ArrayList<>();
        shiftAdapter = new DropDownAdapter(context, R.layout.row_spinner, shifts);
        DropDownModel model1 = new DropDownModel();
        model1.setId(0);
        model1.setText("Select Shift");
        shifts.add(model1);
        shiftList.setAdapter(shiftAdapter);
        GetShiftList();

        shiftList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedShift = shiftAdapter.getItem(position);
                if (selectedShift != null) {
                    if (selectedShift.getId() != -0) {
                        shiftId = selectedShift.getId();
                        shiftName = selectedShift.getText();
                        if (selectedClass.getId() != 0) {
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

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void chooseShift() {
        Intent timetableIntent = new Intent(context, TimetableModuleActivity.class);
        timetableIntent.putExtra("classId", classId);
        timetableIntent.putExtra("className", className);
        timetableIntent.putExtra("shiftId", shiftId);
        timetableIntent.putExtra("shiftName", shiftName);
        timetableIntent.putExtra("timetableOf", timetableOf);
        startActivity(timetableIntent);
    }

    private void GetClassesList() {

        baseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean classBean = response.body();
                if (classBean != null) {

                    if (classBean.rcode == Constants.Rcode.OK) {
                        for (ClassModel datum : classBean.data) {
                            DropDownModel cls = new DropDownModel();
                            cls.setId(datum.Id);
                            cls.setText(datum.Name);
                            classes.add(cls);
                        }
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, " Class list Could not load class. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
            }
        });
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
                        if (size==0) {
                            noRecord.setVisibility(View.VISIBLE);
                            noRecordMsg.setText("No record found.");

                        } else {
                            shiftId = resp.data.get(0).Id;
                            shiftName = resp.data.get(0).ShiftName;
                        }
                    }

                    shifts.clear();
                    if (resp.rcode == Constants.Rcode.OK) {
                        List<SchoolShiftsModel> allShift = resp.data;

                        for (SchoolShiftsModel shift : allShift) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(shift.Id);
                            dropDownModel.setText(shift.ShiftName);
                            shifts.add(dropDownModel);
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
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.showTimetableBtn:
              /*  if ((selectedShift.getId() != 0 && selectedClass.getId() != 0)) {
                    chooseShift();
                } else {
                    Toast.makeText(context, "Please select class", Toast.LENGTH_SHORT).show();
                }*/
                if ((shiftId != 0 && classId != 0)) {
                    chooseShift();
                } else {
                    Toast.makeText(context, "Please select class", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
