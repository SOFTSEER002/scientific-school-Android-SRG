package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Exam.model.ClassWiseSubjectModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowClassSubjectBinding;

import java.util.List;

public class ClassSubjectAdapter extends RecyclerView.Adapter<ClassSubjectAdapter.MyViewHolder> {

    Context mContext;
    private List<ClassWiseSubjectModel> classData;

    @Override
    public int getItemCount() {
        return classData.size();
    }

    public ClassSubjectAdapter(Context context, List<ClassWiseSubjectModel> classSubjectList) {
        super();
        mContext = context;
        classData = classSubjectList;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view;

        RowClassSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_class_subject, parent, false);
        return new ClassSubjectAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassWiseSubjectModel classWiseSubjectModel = classData.get(position);
        ((MyViewHolder) holder).bind(classWiseSubjectModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName;
        public RowClassSubjectBinding itemBinding;

        MyViewHolder(RowClassSubjectBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ClassWiseSubjectModel model) {
            itemBinding.setSubject(model);
     /*       if (model.teacherName == null || model.teacherName.equals("")) {
                itemBinding.teacherName.setText("NA");
            }
            else {
                itemBinding.teacherName.setText(model.teacherName);
            }*/
        }

    }
}


