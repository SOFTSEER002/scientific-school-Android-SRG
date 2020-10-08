package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowStaffOnLeaveBinding;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveModel;

import java.util.ArrayList;

public class StaffOnLeaveTodayAdapter extends RecyclerView.Adapter {

    //Created by babulal
    Context mContext;
    private ArrayList<MonthWiseLeaveModel> teachers;

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public StaffOnLeaveTodayAdapter(Context context, ArrayList<MonthWiseLeaveModel> teacherList) {
        super();
        mContext = context;
        teachers = teacherList;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RowStaffOnLeaveBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_staff_on_leave, parent, false);
        return new StaffOnLeaveTodayAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthWiseLeaveModel teacherdata = teachers.get(position);
        ((MyViewHolder) holder).bind(teacherdata);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public RowStaffOnLeaveBinding itemBinding;

        MyViewHolder(RowStaffOnLeaveBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;


        }

        void bind(final MonthWiseLeaveModel model) {
            itemBinding.setOnLeve(model);

        }

    }

}


