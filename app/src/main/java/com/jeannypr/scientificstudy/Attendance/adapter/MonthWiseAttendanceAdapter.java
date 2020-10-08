package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Attendance.model.MonthAttendanceModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowMonthWiseStudentAttendanceBinding;

import java.util.List;

public class MonthWiseAttendanceAdapter extends RecyclerView.Adapter {

    OnItemClickListener listener;
    Context mContext;

    private List<MonthAttendanceModel> students;

    public MonthWiseAttendanceAdapter(Context context, List<MonthAttendanceModel> studentList, OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        students = studentList;
        listener = itemClickListener;

    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  View view;
        RowMonthWiseStudentAttendanceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_month_wise_student_attendance, parent, false);
       /* view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_wise_attendance, parent, false);*/
        return new MonthWiseAttendanceAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthAttendanceModel monthAttendanceModel = (MonthAttendanceModel) students.get(position);
        ((MonthWiseAttendanceAdapter.MyViewHolder) holder).bind(monthAttendanceModel, listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        /*TextView monthName, totalPresent, totalAbsent, studentRoll, totalHalfday;
        ConstraintLayout attendanceRow;*/

        public RowMonthWiseStudentAttendanceBinding itemBinding;

        MyViewHolder(RowMonthWiseStudentAttendanceBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

           /* monthName = itemView.findViewById(R.id.monthName);
            totalPresent = itemView.findViewById(R.id.totalPresent);
            totalAbsent = itemView.findViewById(R.id.totalAbsent);
            studentRoll = itemView.findViewById(R.id.studentRoll);
            totalHalfday = itemView.findViewById(R.id.totalHalfday);
            totalHalfday.setVisibility(View.GONE);
            attendanceRow = itemView.findViewById(R.id.attendanceRow);*/

        }

        void bind(final MonthAttendanceModel model, final OnItemClickListener listener) {
            itemBinding.setAttendance(model);
           /* itemBinding.monthName.setText(model.fullName != null ? model.fullName : "");
            itemBinding.totalPresent.setText(String.valueOf(model.Present));
            itemBinding.totalAbsent.setText(String.valueOf(model.Absent));
            itemBinding.studentRoll.setText(String.valueOf(model.RollNo));*/

            itemBinding.studentRoll.setText("Roll No.- " + String.valueOf(model.RollNo));

            itemBinding.attendanceRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(model);
                }
            });

            if (model.StudentId == 0) {
                itemBinding.showCalenderBtn.setVisibility(View.GONE);
            }

          /*  monthName.setText(model.fullName != null ? model.fullName : "");
            totalPresent.setText(String.valueOf(model.Present));
            totalAbsent.setText(String.valueOf(model.Absent));
            studentRoll.setText(String.valueOf(model.RollNo));

            attendanceRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(model);
                }
            });*/

        }

    }

    public interface OnItemClickListener {
        void onItemClick(MonthAttendanceModel model);
    }
}


