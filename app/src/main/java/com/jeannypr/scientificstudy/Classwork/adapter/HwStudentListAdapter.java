package com.jeannypr.scientificstudy.Classwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Classwork.model.AssignmentStudentsModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowHwStudentWorkBinding;

import java.util.ArrayList;

public class HwStudentListAdapter extends RecyclerView.Adapter {

    private ArrayList<AssignmentStudentsModel> dataSet;
    private Context mContext;
    private OnItemClickListener listener;

    public HwStudentListAdapter(Context context, ArrayList<AssignmentStudentsModel> data, OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowHwStudentWorkBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_hw_student_work,
                parent, false);
        return new HwStudentListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AssignmentStudentsModel classworkModel = dataSet.get(position);
        ((HwStudentListAdapter.ViewHolder) holder).bind(classworkModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowHwStudentWorkBinding itemBinding;
        // RequestOptions options;

        public ViewHolder(RowHwStudentWorkBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final AssignmentStudentsModel dataModel, final HwStudentListAdapter.OnItemClickListener itemListener) {
            itemBinding.studentName.setText(dataModel.getName());
            itemBinding.studentStatus.setText(dataModel.getHomeworkCurrentStatus());

            itemBinding.chkBox.setChecked(dataModel.isChecked);
            itemBinding.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dataModel.isChecked = b;
                }
            });

            itemBinding.studentRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onItemClick(dataModel);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AssignmentStudentsModel dataModel);
    }
}