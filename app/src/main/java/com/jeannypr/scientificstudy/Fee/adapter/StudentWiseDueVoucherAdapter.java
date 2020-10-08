package com.jeannypr.scientificstudy.Fee.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.model.VoucherDueModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowVoucherDueBinding;

import java.util.List;

public class StudentWiseDueVoucherAdapter extends RecyclerView.Adapter<StudentWiseDueVoucherAdapter.MyViewHolder> {

    Context mContext;
    private List<VoucherDueModel> voucherDueModelList;


    @Override

    public int getItemCount() {
        return voucherDueModelList.size();
    }

    public StudentWiseDueVoucherAdapter(Context context, List<VoucherDueModel> voucherData) {
        super();
        mContext = context;
        voucherDueModelList = voucherData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowVoucherDueBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_voucher_due, parent, false);
        return new StudentWiseDueVoucherAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VoucherDueModel voucherDueModel = voucherDueModelList.get(position);
        voucherDueModel.adapterPosition = position;
        ((MyViewHolder) holder).bind(voucherDueModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowVoucherDueBinding voucherItemBinding;

        MyViewHolder(RowVoucherDueBinding binding) {
            super(binding.getRoot());
            voucherItemBinding = binding;
        }

        void bind(final VoucherDueModel model) {
            voucherItemBinding.setVoucherDue(model);
           /* voucherItemBinding.voucherName.setText(model.VoucherInstallment);
            voucherItemBinding.voucherAmount.setText(String.valueOf(model.TotalAmountDue));*/
        }
    }
}