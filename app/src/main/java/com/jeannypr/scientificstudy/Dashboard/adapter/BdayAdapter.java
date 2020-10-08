package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowBdayChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class BdayAdapter extends RecyclerView.Adapter<BdayAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    BdayInterface mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public BdayAdapter(Context context, ArrayList<HomeTabItemDetail> data, BdayInterface listner) {
        super();
        mContext = context;
        list = data;
        this.mNavigator = listner;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowBdayChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_bday_child_home_tab, parent, false);
        return new BdayAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail itemDetail = list.get(position);
        itemDetail.adapterPosition = position;
        holder.bind(itemDetail);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowBdayChildHomeTabBinding itemBinding;

        MyViewHolder(RowBdayChildHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabItemDetail model) {
            itemBinding.setViewModel(model);

            itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.showFullDesc(model.getDescription(),model.getSubtitle());
                }
            });

        }
    }

    public interface BdayInterface {
        void showFullDesc(String description, String title);
    }
}