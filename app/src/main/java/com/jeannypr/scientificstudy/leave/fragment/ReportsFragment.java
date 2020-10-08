package com.jeannypr.scientificstudy.leave.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.leave.activity.MonthWiseLeaveReportActivity;
import com.jeannypr.scientificstudy.leave.activity.StaffOnLeaveTodayReportActivity;
import com.jeannypr.scientificstudy.leave.activity.StaffWiseMonthLeaveSummaryActivity;

public class ReportsFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private ConstraintLayout staffOnLeaveBtn, monthStaffLeaveBtn, monthLeaveSummaryBtn;
    boolean isApprover;
    ImageView staffLeaveTodayIc, monthStaffLeaveIc, monthLeaveSummaryIc;
    TextView staffLeaveToday, monthStaffLeave, monthLeaveSummary;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            isApprover = bundle.getBoolean("isApprover");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reports,
                container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        monthLeaveSummary = view.findViewById(R.id.monthLeaveSummary);
        monthStaffLeave = view.findViewById(R.id.monthStaffLeave);
        staffLeaveToday = view.findViewById(R.id.staffLeaveToday);
        staffLeaveTodayIc = view.findViewById(R.id.staffLeaveTodayIc);
        monthStaffLeaveIc = view.findViewById(R.id.monthStaffLeaveIc);
        monthLeaveSummaryIc = view.findViewById(R.id.monthLeaveSummaryIc);

        monthLeaveSummary.setOnClickListener(this);
        monthStaffLeave.setOnClickListener(this);
        staffLeaveToday.setOnClickListener(this);
        staffLeaveTodayIc.setOnClickListener(this);
        monthStaffLeaveIc.setOnClickListener(this);
        monthLeaveSummaryIc.setOnClickListener(this);

     /*   staffOnLeaveBtn = view.findViewById(R.id.staffOnLeaveBtn);
        staffOnLeaveBtn.setOnClickListener(this);

        monthStaffLeaveBtn = view.findViewById(R.id.monthStaffLeaveBtn);
        monthStaffLeaveBtn.setOnClickListener(this);

        monthLeaveSummaryBtn=view.findViewById(R.id.monthLeaveSummaryBtn);
        monthLeaveSummaryBtn.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.staffLeaveTodayIc:
            case R.id.staffLeaveToday:
                Intent teacherIntent = new Intent(context, StaffOnLeaveTodayReportActivity.class);
                startActivity(teacherIntent);
                break;

            case R.id.monthStaffLeaveIc:
            case R.id.monthStaffLeave:
                Intent monthStaffIntent = new Intent(context, MonthWiseLeaveReportActivity.class);
                startActivity(monthStaffIntent);
                break;

            case R.id.monthLeaveSummaryIc:
            case R.id.monthLeaveSummary:
                Intent monthLeaveIntent = new Intent(context, StaffWiseMonthLeaveSummaryActivity.class);
                startActivity(monthLeaveIntent);
                break;

           /* case R.id.staffOnLeaveBtn:
                Intent teacherIntent = new Intent(context, StaffOnLeaveTodayReportActivity.class);
                startActivity(teacherIntent);
                break;

            case R.id.monthStaffLeaveBtn:
                Intent monthStaffIntent = new Intent(context, MonthWiseLeaveReportActivity.class);
                startActivity(monthStaffIntent);
                break;

            case R.id.monthLeaveSummaryBtn:
                Intent monthLeaveIntent = new Intent(context, StaffWiseMonthLeaveSummaryActivity.class);
                startActivity(monthLeaveIntent);
                break;*/
        }
    }
}