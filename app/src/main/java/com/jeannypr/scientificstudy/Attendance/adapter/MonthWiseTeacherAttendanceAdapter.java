package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.MonthTeacherAttendanceModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class MonthWiseTeacherAttendanceAdapter extends RecyclerView.Adapter {

    OnItemClickListener listener;
    Context mContext;
    private List<MonthTeacherAttendanceModel> teachers;

    public MonthWiseTeacherAttendanceAdapter(Context context, List<MonthTeacherAttendanceModel> teacherList, OnItemClickListener itemClickListener) {
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
                .inflate(R.layout.row_month_wise_teacher_attendance, parent, false);
        return new MonthWiseTeacherAttendanceAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthTeacherAttendanceModel teacherdata = (MonthTeacherAttendanceModel) teachers.get(position);
        ((MyViewHolder) holder).bind(teacherdata, listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView monthName, totalPresent, totalAbsent, studentRoll, totalHalfday;
        ConstraintLayout attendanceRow;
        ImageView showCalenderBtn;

        MyViewHolder(View itemView) {
            super(itemView);

            monthName = itemView.findViewById(R.id.monthName);
            totalPresent = itemView.findViewById(R.id.totalPresent);
            totalAbsent = itemView.findViewById(R.id.totalAbsent);
            totalHalfday = itemView.findViewById(R.id.totalHalfday);
            attendanceRow = itemView.findViewById(R.id.attendanceRow);
            showCalenderBtn = itemView.findViewById(R.id.showCalenderBtn);

        }

        void bind(final MonthTeacherAttendanceModel model, OnItemClickListener listener) {
            monthName.setText(model.getTeacherName().substring(0,1).toUpperCase()+model.getTeacherName().substring(1).toLowerCase());
            totalPresent.setText(String.valueOf(model.getPresent()));
            totalAbsent.setText(String.valueOf(model.getAbsent()));
            totalHalfday.setText(String.valueOf(model.getHalfDay()));
            showCalenderBtn.setVisibility(View.GONE);
           /* attendanceRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MonthWiseTeacherAttendanceAdapter.this.listener.onItemClick(model);
                }
            });*/

        }

    }

    public interface OnItemClickListener {
        void onItemClick(MonthTeacherAttendanceModel model);
    }

}


