package com.jeannypr.scientificstudy.Attendance.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.jeannypr.scientificstudy.Attendance.activity.DayWiseTeacherAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.ExtraDayAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.MonthWiseTeacherAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.OutDoorAttendanceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.StaffWiseAttenenceActivity;
import com.jeannypr.scientificstudy.Attendance.activity.TeacherAttendanceActivity;
import com.jeannypr.scientificstudy.R;

public class TeacherAttendanceModuleFragment extends Fragment implements View.OnClickListener {
    //  CardView dayWiseAttendance, takeAttendance, monthWiseAttendance, extraDayReport, outDoorReport, staffWiseAttendance, adminSelfAttendance;
    ImageView dayWiseAttendanceIc, monthWiseAttendanceIc, staffWiseAttendanceIc, extraDayReportIc, outDoorReportIc;
    TextView dayWiseAttendance, monthWiseAttendance, staffWiseAttendance, extraDayReport, outDoorReport;
    MaterialButton takeAttendance;
    ConstraintLayout dayWiseAttendancetRow, monthAttendanceReportRow, staffWiseAttendanceReportRow, extraDayReportReportRow, outDoorReportRow;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_attendance_home,
                container, false);
        context = getActivity();

        takeAttendance = view.findViewById(R.id.takeAttendance);

        dayWiseAttendancetRow = view.findViewById(R.id.dayWiseAttendancetRow);
        monthAttendanceReportRow = view.findViewById(R.id.monthAttendanceReportRow);
        staffWiseAttendanceReportRow = view.findViewById(R.id.staffWiseAttendanceReportRow);
        extraDayReportReportRow = view.findViewById(R.id.extraDayReportReportRow);
        outDoorReportRow = view.findViewById(R.id.outDoorReportRow);

        dayWiseAttendancetRow.setOnClickListener(this);
        monthAttendanceReportRow.setOnClickListener(this);
        staffWiseAttendanceReportRow.setOnClickListener(this);
        extraDayReportReportRow.setOnClickListener(this);
        outDoorReportRow.setOnClickListener(this);

        takeAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent teacherattendanceIntent = new Intent(context, TeacherAttendanceActivity.class);
                startActivity(teacherattendanceIntent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dayWiseAttendancetRow:
                Intent daywiseIntent = new Intent(context, DayWiseTeacherAttendanceActivity.class);
                startActivity(daywiseIntent);
                break;

            case R.id.monthAttendanceReportRow:
                Intent monthWiseIntent = new Intent(context, MonthWiseTeacherAttendanceActivity.class);
                startActivity(monthWiseIntent);
                break;

            case R.id.outDoorReportRow:
                Intent outDoorIntent = new Intent(context, OutDoorAttendanceActivity.class);
                startActivity(outDoorIntent);
                break;

            case R.id.extraDayReportReportRow:
                Intent extradayIntent = new Intent(context, ExtraDayAttendanceActivity.class);
                startActivity(extradayIntent);
                break;

            case R.id.staffWiseAttendanceReportRow:
                Intent staffIntent = new Intent(context, StaffWiseAttenenceActivity.class);
                startActivity(staffIntent);
                break;
        }
    }
}
