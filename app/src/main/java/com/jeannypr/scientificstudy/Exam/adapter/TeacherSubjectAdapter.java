package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Exam.model.TeacherSubjectModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowTeacherSubjectBinding;

import java.util.List;

public class TeacherSubjectAdapter extends RecyclerView.Adapter<TeacherSubjectAdapter.MyViewHolder> {

    Context mContext;
    private List<TeacherSubjectModel> teacherData;

    @Override
    public int getItemCount() {
        return teacherData.size();
    }

    public TeacherSubjectAdapter(Context context, List<TeacherSubjectModel> teacherSubjectList) {
        super();
        mContext = context;
        teacherData = teacherSubjectList;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view;

        RowTeacherSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_teacher_subject, parent, false);
        return new TeacherSubjectAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TeacherSubjectModel teacherSubjectModel = teacherData.get(position);
        ((MyViewHolder) holder).bind(teacherSubjectModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowTeacherSubjectBinding itemBinding;

        MyViewHolder(RowTeacherSubjectBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final TeacherSubjectModel model) {
            itemBinding.setTeacher(model);
        }

    }


}


