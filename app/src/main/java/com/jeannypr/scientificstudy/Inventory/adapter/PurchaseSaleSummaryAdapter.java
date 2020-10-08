package com.jeannypr.scientificstudy.Inventory.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowPurcahseSaleSummaryBinding;

import java.util.List;

public class PurchaseSaleSummaryAdapter extends RecyclerView.Adapter {

    OnItemClickListener listener;
    Context mContext;

    private List<PurchaseSaleSummaryModel> purchaseItem;

    public PurchaseSaleSummaryAdapter(Context context, List<PurchaseSaleSummaryModel> purchaseList, OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        purchaseItem = purchaseList;
        listener = itemClickListener;

    }

    @Override
    public int getItemCount() {
        return purchaseItem.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowPurcahseSaleSummaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_purcahse_sale_summary, parent, false);
        return new PurchaseSaleSummaryAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PurchaseSaleSummaryModel purchaseModel = (PurchaseSaleSummaryModel) purchaseItem.get(position);
        ((PurchaseSaleSummaryAdapter.MyViewHolder) holder).bind(purchaseModel, listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowPurcahseSaleSummaryBinding itemBinding;

        MyViewHolder(RowPurcahseSaleSummaryBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final PurchaseSaleSummaryModel model, final OnItemClickListener listener) {
            itemBinding.setPurchase(model);
            itemBinding.amount.setText("Rs."+" "+(String.valueOf(model.Amount)));

            itemBinding.purchaseRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(model);
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(PurchaseSaleSummaryModel model);
    }
}


