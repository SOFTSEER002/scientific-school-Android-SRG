package com.jeannypr.scientificstudy.Student.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.model.StudentProfileModel;

import java.util.ArrayList;

public class SubjectTeachersInStudentDetailAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<StudentProfileModel> data;

    public SubjectTeachersInStudentDetailAdapter(Context context, ArrayList<StudentProfileModel> data) {
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.row_subject_teacher_in_student_detail_tab, viewGroup, false);
        return new SubjectTeachersInStudentDetailAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StudentProfileModel subjectAndTeachers = data.get(i);
        subjectAndTeachers.SubjectAdapterPosition = i;
        ((MyHolder) viewHolder).bind(subjectAndTeachers);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView txtStaffName, txtSubjectName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.subjectName);
            txtStaffName = itemView.findViewById(R.id.staffName);
        }

        public void bind(StudentProfileModel data) {
            txtSubjectName.setText(data.getSubjectName());

            String teacherName = data.SubjectAdapterPosition > 0 ? data.getSubjectTeacherName() : data.getSubjectTeacherName();
            txtStaffName.setText(teacherName);
        }
    }
}