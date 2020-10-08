package com.jeannypr.scientificstudy.Fee.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.LedgerReportModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowStudentWiseDueFeeBinding;

import java.util.List;

public class StudentWiseDueReportAdapter extends RecyclerView.Adapter<StudentWiseDueReportAdapter.MyViewHolder> {

    Context mContext;
    private List<LedgerReportModel> ledgerReportModelList;


    @Override

    public int getItemCount() {
        return ledgerReportModelList.size();
    }

    public StudentWiseDueReportAdapter(Context context, List<LedgerReportModel> ledgerData) {
        super();
        mContext = context;
        ledgerReportModelList = ledgerData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowStudentWiseDueFeeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_student_wise_due_fee, parent, false);
        return new StudentWiseDueReportAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LedgerReportModel ledgerReportModel = ledgerReportModelList.get(position);
        ((MyViewHolder) holder).bind(ledgerReportModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowStudentWiseDueFeeBinding itemBinding;


        MyViewHolder(RowStudentWiseDueFeeBinding binding) {
            super(binding.getRoot());
            // itemBinding = binding;
        }

        void bind(final LedgerReportModel model) {
            // itemBinding.setLedger(model);

        }

    }


}


