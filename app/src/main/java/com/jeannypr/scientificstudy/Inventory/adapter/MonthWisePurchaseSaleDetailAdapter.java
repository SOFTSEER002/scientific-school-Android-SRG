package com.jeannypr.scientificstudy.Inventory.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItemModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowMonthwisePurchaseSaleDetailBinding;

import java.util.List;

public class MonthWisePurchaseSaleDetailAdapter extends RecyclerView.Adapter<MonthWisePurchaseSaleDetailAdapter.MyViewHolder> {

    Context mContext;
    private List<PurchaseSaleItemModel> purchaseItem;

    @Override

    public int getItemCount() {
        return purchaseItem.size();
    }

    public MonthWisePurchaseSaleDetailAdapter(Context context, List<PurchaseSaleItemModel> purchaseList) {
        super();
        mContext = context;
        purchaseItem = purchaseList;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowMonthwisePurchaseSaleDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_monthwise_purchase_sale_detail, parent, false);
        return new MonthWisePurchaseSaleDetailAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PurchaseSaleItemModel purchaseModel = purchaseItem.get(position);
        ((MyViewHolder) holder).bind(purchaseModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowMonthwisePurchaseSaleDetailBinding itemBinding;

        MyViewHolder(RowMonthwisePurchaseSaleDetailBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final PurchaseSaleItemModel model) {
            itemBinding.setItem(model);
            itemBinding.rate.setText(String.valueOf(model.RatePerItem));
            itemBinding.itemQuntity.setText("*" + " " + (String.valueOf(model.Quantity)));
            itemBinding.Amount.setText(String.valueOf(model.Amount));

            int width = itemBinding.Amount.getWidth();
            itemBinding.divider.getLayoutParams().width = width;


        }

    }


}


