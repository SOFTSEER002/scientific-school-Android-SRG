package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.StaffWiseAttendanceModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class StaffWiseAttendanceAdapter extends RecyclerView.Adapter {

    OnItemClickListener listener;
    Context mContext;
    private List<StaffWiseAttendanceModel> teachers;

    public StaffWiseAttendanceAdapter(Context context, List<StaffWiseAttendanceModel> teacherList, OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        teachers = teacherList;
        listener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_wise_attendance, parent, false);
        return new StaffWiseAttendanceAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StaffWiseAttendanceModel teacherdata = (StaffWiseAttendanceModel) teachers.get(position);
        ((MyViewHolder) holder).bind(teacherdata, listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView monthName, totalPresent, totalAbsent, studentRoll, totalHalfday;
        ConstraintLayout attendanceRow;
        ImageView showCalenderBtn;
        View divider;

        MyViewHolder(View itemView) {
            super(itemView);

            monthName = itemView.findViewById(R.id.monthName);
            totalPresent = itemView.findViewById(R.id.totalPresent);
            totalAbsent = itemView.findViewById(R.id.totalAbsent);
            /*studentRoll = itemView.findViewById(R.id.studentRoll);
            studentRoll.setVisibility(View.GONE);*/
            totalHalfday = itemView.findViewById(R.id.totalHalfday);
            attendanceRow = itemView.findViewById(R.id.attendanceRow);
            showCalenderBtn = itemView.findViewById(R.id.showCalenderBtn);
            showCalenderBtn.setVisibility(View.GONE);
           /* divider=itemView.findViewById(R.id.divider);
            divider.setVisibility(View.GONE);*/

        }

        void bind(final StaffWiseAttendanceModel model, OnItemClickListener listener) {
            monthName.setText(String.valueOf(model.getMonth().substring(0, 1).toUpperCase() + model.getMonth().substring(1).toLowerCase()));
            totalPresent.setText(String.valueOf(model.getPresent()));
            totalAbsent.setText(String.valueOf(model.getAbsent()));
            totalHalfday.setText(String.valueOf(model.getHalfDay()));
           /* attendanceRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaffWiseAttendanceAdapter.this.listener.onItemClick(model);
                }
            });*/

        }

    }

    public interface OnItemClickListener {
        void onItemClick(StaffWiseAttendanceModel model);
    }

}


