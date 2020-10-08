package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.model.SptWallModel;
import com.jeannypr.scientificstudy.databinding.RowFeeSummaryBinding;

import java.util.ArrayList;
import java.util.List;

public class FeeSummaryAdapter extends RecyclerView.Adapter {
    OnItemClickListener listener;
    Context mContext;
    private List<FeeSummaryModel> feeSummaryModels, filteredList;

    public FeeSummaryAdapter(Context context, List<FeeSummaryModel> models, OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        feeSummaryModels = models;
        filteredList = models;
        listener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFeeSummaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_fee_summary, parent, false);
        return new FeeSummaryAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FeeSummaryModel feeSummaryModel = filteredList.get(position);
        ((MyViewHolder) holder).bind(feeSummaryModel);
    }

    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {
                    ArrayList<FeeSummaryModel> tempData = new ArrayList<>();

                    for (FeeSummaryModel row : feeSummaryModels) {
                        if (row.IsPaid.toString().toLowerCase().contains(charString.toLowerCase())) {
                            tempData.add(row);
                        }
                    }
                    filteredList = tempData;

                } else
                    filteredList = feeSummaryModels;

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<FeeSummaryModel>) filterResults.values;
                notifyDataSetChanged();

                if (filteredList.size() > 0) listener.showSearchMsg(true);
                else listener.showSearchMsg(false);
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public RowFeeSummaryBinding itemBinding;

        MyViewHolder(RowFeeSummaryBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final FeeSummaryModel model) {
            itemBinding.setFee(model);
            String totalPayable = model.TotalAmountPayable.toString();
            String totalPaid = model.TotalAmountPaid.toString();
            String totalDues = model.TotalAmountDue.toString();

            itemBinding.installmentTitle.setText(model.InstallmentTitle == null ? "" : model.InstallmentTitle);
            itemBinding.payableAmount.setText("Payable: " + (model.TotalAmountPayable == null ? "0" : totalPayable));
            itemBinding.paidAmount.setText("Paid: " + (model.TotalAmountPaid == null ? "0" : totalPaid));
            itemBinding.dueAmount.setText("Due: " + (model.TotalAmountDue == null ? "0" : totalDues));
            itemBinding.installmentDate.setText(model.InstallmentDate == null ? "" : model.InstallmentDate);

            itemBinding.rowContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(model);
                }
            });

            String paymentStatus = model.PaymentStatus;
            if (paymentStatus.equals(Constants.PaymentStatus.PAID)) {
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.due_collection_green_bg));
                itemBinding.paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.due_row_green_bg));
                itemBinding.installmentDate.setText(model.InstallmentDate == null ? "" : model.InstallmentDate);
                itemBinding.installmentStatus.setText("PAID");

            } else if (paymentStatus.equals(Constants.PaymentStatus.DUE)) {
                itemBinding.installmentStatus.setText("DUE");
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                itemBinding.paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
                itemBinding.installmentDate.setText(model.DueDate == null ? "" : model.DueDate);

            } else {
                itemBinding.installmentStatus.setText("OVERDUE");
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                itemBinding.paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
                itemBinding.installmentDate.setText(model.DueDate == null ? "" : model.DueDate);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FeeSummaryModel model);

        void showSearchMsg(Boolean isDataFound);
    }
}


