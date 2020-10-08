package com.jeannypr.scientificstudy.Attendance.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Attendance.model.StudentAttendanceModel;
import com.jeannypr.scientificstudy.R;


import java.util.List;

public class DateWiseAttendanceAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<StudentAttendanceModel> students;

    public DateWiseAttendanceAdapter(Context context, List<StudentAttendanceModel> studentList) {
        super();
        mContext = context;
        students = studentList;

    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_take_attendance_new, parent, false);
        return new DateWiseAttendanceAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StudentAttendanceModel studentAttendanceModel = (StudentAttendanceModel) students.get(position);
        ((MyViewHolder) holder).bind(studentAttendanceModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentRoll, studentAbsent, studentPresent;
        //  RadioGroup stuAattendance;
        RadioButton radPresent, radAbsent;


        MyViewHolder(View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.studentName);
            studentAbsent = itemView.findViewById(R.id.studentAbsent);
            studentPresent = itemView.findViewById(R.id.studentPresent);

            // studentRoll = itemView.findViewById(R.id.studentRoll);
          /*  radPresent = itemView.findViewById(R.id.radPresent);
            radAbsent = itemView.findViewById(R.id.radAbsent);
            // stuAattendance = itemView.findViewById(R.id.stuAttendance);
            radAbsent.setClickable(false);
            radPresent.setClickable(false);*/

        }

        void bind(final StudentAttendanceModel student) {
            studentName.setText(student.getName() != null ? student.getName().substring(0, 1).toUpperCase() + student.getName().substring(1).toLowerCase() : "");
            //studentRoll.setText(String.valueOf(student.getRoll()));

            if (student.getPresent() != null) {
                if (student.getPresent()) {
                    studentPresent.setVisibility(View.VISIBLE);
                    studentAbsent.setVisibility(View.GONE);
                    studentPresent.setText("Present");

                } else if (!student.getPresent()) {
                    studentPresent.setVisibility(View.GONE);
                    studentAbsent.setVisibility(View.VISIBLE);
                    studentAbsent.setText("Absent");
                }
            } else {
                studentPresent.setVisibility(View.GONE);
                studentAbsent.setVisibility(View.GONE);
            }

          /*  if (student.getPresent() != null) {
                if (student.getPresent()) {

                    radPresent.setButtonDrawable(R.drawable.present_checked);
                    radAbsent.setButtonDrawable(R.drawable.absent_states_lg);

                } else if (!student.getPresent()) {
                    radAbsent.setButtonDrawable(R.drawable.absent_checked);
                    radPresent.setButtonDrawable(R.drawable.present_states_lg);
                }
            } else {
                radPresent.setButtonDrawable(R.drawable.present_states_lg);
                radAbsent.setButtonDrawable(R.drawable.absent_states_lg);
            }*/
        }

    }


}


