
package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowSpecialEventChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class SpecialEventAdapter extends RecyclerView.Adapter<SpecialEventAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    SpecialEventInterface mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SpecialEventAdapter(Context context, ArrayList<HomeTabItemDetail> data, SpecialEventInterface listner) {
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

        RowSpecialEventChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_special_event_child_home_tab, parent, false);
        return new SpecialEventAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail itemDetail = list.get(position);
        itemDetail.adapterPosition = position;
        holder.bind(itemDetail);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowSpecialEventChildHomeTabBinding itemBinding;

        MyViewHolder(RowSpecialEventChildHomeTabBinding binding) {
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

    public interface SpecialEventInterface {
        void showFullDesc(String description, String title);
    }
}