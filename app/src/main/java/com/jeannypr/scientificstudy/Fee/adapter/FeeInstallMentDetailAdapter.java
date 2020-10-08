package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.model.FeeInstallmentDetailModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowFeeInstallmentDetailBinding;

import java.util.List;

public class FeeInstallMentDetailAdapter extends RecyclerView.Adapter {

    Context mContext;
    private List<FeeInstallmentDetailModel> feeInstallmentDetailModels;


    public FeeInstallMentDetailAdapter(Context context, List<FeeInstallmentDetailModel> models) {
        super();
        mContext = context;
        feeInstallmentDetailModels = models;
    }

    @Override
    public int getItemCount() {
        return feeInstallmentDetailModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowFeeInstallmentDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_fee_installment_detail, parent, false);
        return new FeeInstallMentDetailAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FeeInstallmentDetailModel feeSummaryModel = (FeeInstallmentDetailModel) feeInstallmentDetailModels.get(position);
        ((MyViewHolder) holder).bind(feeSummaryModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public RowFeeInstallmentDetailBinding itemBinding;

        MyViewHolder(RowFeeInstallmentDetailBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final FeeInstallmentDetailModel detailModel) {
            itemBinding.setDetail(detailModel);

            String totalPayable = detailModel.AmountPayable.toString();
            String totalPaid = detailModel.AmountPaid.toString();
            String totalDues = detailModel.AmountDue.toString();
            String totalDiscount = detailModel.Discount.toString();

            itemBinding.feeType.setText(detailModel.Title == null ? "" : detailModel.Title);
            itemBinding.payableAmount.setText(detailModel.AmountPayable == null ? "0" : totalPayable);
            itemBinding.paidAmount.setText(detailModel.AmountPaid == null ? "0" : totalPaid);
            itemBinding.dueAmount.setText(detailModel.AmountDue == null ? "0" : totalDues);
            itemBinding.discount.setText(detailModel.Discount == null ? "0" : totalDiscount);

            int due = detailModel.AmountDue;
            int payable = detailModel.AmountPayable;

            if (due == 0) {
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.due_collection_green_bg));
                itemBinding.payment.setBackground(mContext.getResources().getDrawable(R.color.green_light));
                itemBinding.payAmount.setText("Paid");
                itemBinding.payAmount.setTextColor(mContext.getResources().getColor(R.color.mGreen));

            } else if (due < payable) {
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                itemBinding.payment.setBackground(mContext.getResources().getDrawable(R.color.rea_light));
                itemBinding.payAmount.setText("Dues");
                itemBinding.payAmount.setTextColor(mContext.getResources().getColor(R.color.mRed));

            } else {

                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                itemBinding.payment.setBackground(mContext.getResources().getDrawable(R.color.rea_light));
                itemBinding.payAmount.setText("Overdue");
                itemBinding.payAmount.setTextColor(mContext.getResources().getColor(R.color.mRed));
            }

        }
    }
}


