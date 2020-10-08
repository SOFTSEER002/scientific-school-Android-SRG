package com.jeannypr.scientificstudy.leave.adapter;
//Created by babulal
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowStaffWiseMonthLeaveBinding;
import com.jeannypr.scientificstudy.leave.model.MonthLeaveSummaryModel;

import java.util.ArrayList;
import java.util.List;

public class StaffWiseMonthLeaveSummaryAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<MonthLeaveSummaryModel> monthLeaveList;

    @Override
    public int getItemCount() {
        return monthLeaveList.size();
    }

    public StaffWiseMonthLeaveSummaryAdapter(Context context, List<MonthLeaveSummaryModel> teacherList) {
        super();
        mContext = context;
        monthLeaveList = teacherList;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view;


        RowStaffWiseMonthLeaveBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_staff_wise_month_leave, parent, false);
        return new StaffWiseMonthLeaveSummaryAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthLeaveSummaryModel monthLeaveSummaryModel = monthLeaveList.get(position);
        ((MyViewHolder) holder).bind(monthLeaveSummaryModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowStaffWiseMonthLeaveBinding itemBinding;

        TextView staffName, daysLeave;

        MyViewHolder(RowStaffWiseMonthLeaveBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
/*
            staffName = itemView.findViewById(R.id.staffName);
            daysLeave = itemView.findViewById(R.id.daysLeave);*/


        }

        void bind(final MonthLeaveSummaryModel model) {
            itemBinding.setSummaryLeave(model);
            /*staffName.setText(String.valueOf(model.getStaffName()));
            daysLeave.setText(String.valueOf(model.getLeaveDays()));*/

        }

    }

    public void FilterSatffName(ArrayList<MonthLeaveSummaryModel> filterdNames) {
        monthLeaveList = filterdNames;
        notifyDataSetChanged();
    }

}


