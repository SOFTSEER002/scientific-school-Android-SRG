package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowMonthWiseLeaveBinding;
import com.jeannypr.scientificstudy.leave.model.MonthWiseLeaveModel;

import java.util.ArrayList;

/*
 * Author : Babulal
 * Date :
 * Month wise teacher adapter
 */

public class MonthWiseLeaveAdapter extends RecyclerView.Adapter {


    Context mContext;
    private ArrayList<MonthWiseLeaveModel> teachers;

    @Override
    public int getItemCount() {
        return teachers.size();
    }


    public MonthWiseLeaveAdapter(Context context, ArrayList<MonthWiseLeaveModel> models) {
        super();
        mContext = context;
        teachers = models;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMonthWiseLeaveBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_month_wise_leave, parent, false);
        return new MonthWiseLeaveAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MonthWiseLeaveModel teacherdata = teachers.get(position);
        ((MyViewHolder) holder).bind(teacherdata);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowMonthWiseLeaveBinding itemBinding;

        MyViewHolder(RowMonthWiseLeaveBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final MonthWiseLeaveModel model) {
            itemBinding.setMonthLeave(model);

        }

    }

    public void FilterSatffName(ArrayList<MonthWiseLeaveModel> filterdNames) {
        teachers = filterdNames;
        notifyDataSetChanged();
    }

}


