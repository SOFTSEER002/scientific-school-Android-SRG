package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.model.MonthwiseAttendanceModel;

import java.util.List;

public class StudentWiseAttendanceAdapter extends RecyclerView.Adapter {

    Context mContext;
    private List<MonthwiseAttendanceModel> attendanceReport;
    OnItemClickListener listener;

    public StudentWiseAttendanceAdapter(Context context, List<MonthwiseAttendanceModel> model, StudentWiseAttendanceAdapter.OnItemClickListener clickListener) {
        super();
        mContext = context;
        attendanceReport = model;
        listener = clickListener;
    }

    @Override
    public int getItemCount() {
        return attendanceReport.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_wise_attendance, parent, false);
        return new StudentWiseAttendanceAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthwiseAttendanceModel route = (MonthwiseAttendanceModel) attendanceReport.get(position);
        ((MyViewHolder) holder).bind(route, listener);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView monthName, txtTotalPresent, txtTotalAbsent, studentRoll;
        ConstraintLayout attendanceRow;
        // ImageView showCalenderBtn;

        MyViewHolder(View itemView) {
            super(itemView);

          //  studentRoll = itemView.findViewById(R.id.studentRoll);
            monthName = itemView.findViewById(R.id.monthName);
            txtTotalPresent = itemView.findViewById(R.id.totalPresent);
            txtTotalAbsent = itemView.findViewById(R.id.totalAbsent);
            attendanceRow = itemView.findViewById(R.id.attendanceRow);
            // showCalenderBtn = itemView.findViewById(R.id.showCalenderBtn);
        }

        void bind(final MonthwiseAttendanceModel model, final StudentWiseAttendanceAdapter.OnItemClickListener listener) {
            monthName.setText(model.Month);
          //  studentRoll.setVisibility(View.GONE);

            int totalDigits = (int) Math.log10(model.Absent) + 1;
            String totalAbsent = totalDigits < 2 ? "0" + String.valueOf(model.Absent) : String.valueOf(model.Absent);
            txtTotalAbsent.setText(totalAbsent);

            int totalDigitsInPresent = (int) Math.log10(model.Present) + 1;
            String totalPresent = totalDigitsInPresent < 2 ? "0" + String.valueOf(model.Present) : String.valueOf(model.Present);
            txtTotalPresent.setText(totalPresent);

            attendanceRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(model);
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onClick(MonthwiseAttendanceModel teacherModel);
    }
}