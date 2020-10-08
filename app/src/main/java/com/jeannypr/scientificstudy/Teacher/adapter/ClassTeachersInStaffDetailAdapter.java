package com.jeannypr.scientificstudy.Teacher.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.model.StaffProfileModel;

import java.util.ArrayList;

public class ClassTeachersInStaffDetailAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<StaffProfileModel> data;

    public ClassTeachersInStaffDetailAdapter(Context context, ArrayList<StaffProfileModel> data) {
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.row_subject_teacher_in_student_detail_tab, viewGroup, false);
        return new ClassTeachersInStaffDetailAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StaffProfileModel staffProfileModel = data.get(i);
        staffProfileModel.SubjectAdapterPosition = i;
        ((MyHolder) viewHolder).bind(staffProfileModel);
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

        public void bind(StaffProfileModel data) {
            txtSubjectName.setText(data.getSubject());
            txtStaffName.setText(data.getClassName());
        }
    }
}