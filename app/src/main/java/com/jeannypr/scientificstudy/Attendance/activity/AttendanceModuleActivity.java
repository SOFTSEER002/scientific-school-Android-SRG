package com.jeannypr.scientificstudy.Attendance.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.jeannypr.scientificstudy.Attendance.fragment.StudentAttendanceModuleFragment;
import com.jeannypr.scientificstudy.Attendance.fragment.TeacherAttendanceModuleFragment;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class AttendanceModuleActivity extends AppCompatActivity {

    private Spinner classList;
    DropDownModel selectedItem;
    private DropDownAdapter adapter;
    private Context context;
    private ProgressDialog p_dialog;
    RadioButton radTeacher, radStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_module);
        context = this;

        RelativeLayout fragmentContainer = findViewById(R.id.fragment_container);
        radTeacher = findViewById(R.id.radTeacher);
        radStudent = findViewById(R.id.radStudent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Attendance", "");

        SwitchFragment(false);

        RadioGroup radioGroup = findViewById(R.id.radAudience);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radStudent.setChecked(checkedId == R.id.radStudent);
                radTeacher.setChecked(checkedId == R.id.radTeacher);
                SwitchFragment(checkedId == R.id.radStudent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SwitchFragment(Boolean isStudent) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment teacherFragment = isStudent ? new StudentAttendanceModuleFragment() : new TeacherAttendanceModuleFragment();
            ft.replace(R.id.fragment_container, teacherFragment, isStudent ? "student" : "teacher");
            ft.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
