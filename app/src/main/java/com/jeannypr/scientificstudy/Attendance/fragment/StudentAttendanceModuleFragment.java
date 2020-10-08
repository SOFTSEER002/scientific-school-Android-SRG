package com.jeannypr.scientificstudy.Attendance.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.jeannypr.scientificstudy.Attendance.activity.DateWiseStudentAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.MonthWiseStudentAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.StudentWiseAttenenceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.TakeStudentAttendanceActivity;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAttendanceModuleFragment extends Fragment implements View.OnClickListener {

    private Spinner classList;
    private DropDownModel selectedItem;
    private DropDownAdapter adapter;
    private Context context;
    private View view;
    private ProgressBar pb;
    int classId, studentId;
    String className;
    ArrayList<DropDownModel> classes;
    ConstraintLayout dateWiseAttendancetRow, studentAttendanceReportRow, monthWiseAttendancetRow;
    MaterialButton takeAttendance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_attendance_home,
                container, false);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        takeAttendance = view.findViewById(R.id.takeAttendance);
        dateWiseAttendancetRow = view.findViewById(R.id.dateWiseAttendancetRow);
        studentAttendanceReportRow = view.findViewById(R.id.studentAttendanceReportRow);
        monthWiseAttendancetRow = view.findViewById(R.id.monthWiseAttendancetRow);

        dateWiseAttendancetRow.setOnClickListener(this);
        studentAttendanceReportRow.setOnClickListener(this);
        monthWiseAttendancetRow.setOnClickListener(this);
        takeAttendance.setOnClickListener(this);

        classes = new ArrayList<>();
        DropDownModel defaultOption = new DropDownModel();
        defaultOption.setText(Constants.DEFAULT_CLASS);
        defaultOption.setId(-1);
        classes.add(defaultOption);

        adapter = new DropDownAdapter(context, R.layout.row_spinner, classes);
        classList = view.findViewById(R.id.ddlClassList);
        classList.setAdapter(adapter);
        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                selectedItem = adapter.getItem(position);

                if (selectedItem != null) {
                    if (selectedItem.getId() == -1) {
                        selectedItem = null;
                        classId = -1;
                        className = "";

                    } else {
                        classId = selectedItem.getId();
                        className = selectedItem.getText();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        GetClasses();
    }

    public void GetClasses() {
        BaseService BaseService = new DataRepo<>(BaseService.class, context).getService();
        UserPreference userPref = UserPreference.getInstance(context);
        UserModel userData = userPref.getUserData();

        BaseService.getClasses(userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            classes.add(dropDownModel);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                Log.d("classList", "server call error");
                Toast.makeText(context, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.studentAttendanceReportRow:
                if (classId == -1) {
                    Toast.makeText(context, "Please select class to get student wise attendance report", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(context, StudentWiseAttenenceActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("className", className);
                startActivity(intent);
                break;

            case R.id.dateWiseAttendancetRow:
                if (selectedItem == null) {
                    Toast.makeText(context, "No Class Selected", Toast.LENGTH_LONG).show();
                } else {
                    Intent dateWiseAttendance = new Intent(context, DateWiseStudentAttendanceActivity.class);
                    dateWiseAttendance.putExtra("classId", classId);
                    dateWiseAttendance.putExtra("className", className);
                    dateWiseAttendance.putExtra("studentId", studentId);
                    startActivity(dateWiseAttendance);
                }
                break;

            case R.id.monthWiseAttendancetRow:
                if (selectedItem == null) {
                    Toast.makeText(context, "No class Selected", Toast.LENGTH_LONG).show();
                } else {
                    Intent monthWiseAttendance = new Intent(context, MonthWiseStudentAttendanceActivity.class);
                    monthWiseAttendance.putExtra("classId", classId);
                    monthWiseAttendance.putExtra("className", className);
                    startActivity(monthWiseAttendance);
                }
                break;

            case R.id.takeAttendance:
                if (selectedItem == null) {
                    Toast.makeText(context, "No Class Selected", Toast.LENGTH_LONG).show();

                } else {
                    Intent takeAttendance = new Intent(context, TakeStudentAttendanceActivity.class);
                    takeAttendance.putExtra("ClassId", selectedItem.getId());
                    takeAttendance.putExtra("ClassName", selectedItem.getText());
                    startActivity(takeAttendance);
                }
        }
    }
}

