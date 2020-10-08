package com.jeannypr.scientificstudy.Fee.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.model.FeeDueModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowStudentWiseDueFeeReportBinding;

import java.util.List;

public class StudentWiseDueFeeReportAdapter extends RecyclerView.Adapter<StudentWiseDueFeeReportAdapter.MyViewHolder> {

    Context mContext;
    private List<FeeDueModel> duesFeeList;


    @Override

    public int getItemCount() {
        return duesFeeList.size();
    }

    public StudentWiseDueFeeReportAdapter(Context context, List<FeeDueModel> dueFeedata) {
        super();
        mContext = context;
        duesFeeList = dueFeedata;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowStudentWiseDueFeeReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_student_wise_due_fee_report, parent, false);
        return new StudentWiseDueFeeReportAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeeDueModel ledgerReportModel = duesFeeList.get(position);
        ledgerReportModel.adapterPosition=position;
        (holder).bind(ledgerReportModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowStudentWiseDueFeeReportBinding feeItemBinding;

        MyViewHolder(RowStudentWiseDueFeeReportBinding binding) {
            super(binding.getRoot());
            feeItemBinding = binding;
        }

        void bind(final FeeDueModel model) {
            feeItemBinding.setStudentFee(model);
           // feeItemBinding.studentNo.setText(String.valueOf(getAdapterPosition()));
            /*feeItemBinding.studentCollection.setText(String.valueOf(model.getTotalAmountDue()));
            feeItemBinding.dateVal.setText(model.getInstallmentTitle());*/
            //itemBinding.VocherNameVal.setText(model.VoucherInstallment);
        }
    }
}


