package com.jeannypr.scientificstudy.Inventory.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleSummaryDateModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowMonthwisePurchaseSaleSummaryBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthWisePurchaseSaleSummaryAdapter extends RecyclerView.Adapter {

    OnItemClickListener listener;
    Context mContext;

    private List<PurchaseSaleSummaryDateModel> purchaseItem;

    public MonthWisePurchaseSaleSummaryAdapter(Context context, List<PurchaseSaleSummaryDateModel> purchaseList, OnItemClickListener itemClickListener) {
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

        RowMonthwisePurchaseSaleSummaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_monthwise_purchase_sale_summary, parent, false);
        return new MonthWisePurchaseSaleSummaryAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PurchaseSaleSummaryDateModel purchaseDateModel = (PurchaseSaleSummaryDateModel) purchaseItem.get(position);
        ((MonthWisePurchaseSaleSummaryAdapter.MyViewHolder) holder).bind(purchaseDateModel, listener);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowMonthwisePurchaseSaleSummaryBinding itemBinding;

        MyViewHolder(RowMonthwisePurchaseSaleSummaryBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final PurchaseSaleSummaryDateModel model, final OnItemClickListener listener) {
            itemBinding.setPurchase(model);
            itemBinding.amount.setText("Rs."+" "+ (String.valueOf(model.Amount)));

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateObj = new SimpleDateFormat("MM/dd/yyyy").parse(model.TransactionDate);
                model.TransactionDate = df.format(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemBinding.Date.setText(model.TransactionDate);
            itemBinding.purchaseRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(model);
                }
            });

        }

    }

    public interface OnItemClickListener {
        void onItemClick(PurchaseSaleSummaryDateModel model);
    }
}


