package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowNewsChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    NewsInterface mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public NewsAdapter(Context context, ArrayList<HomeTabItemDetail> data, NewsInterface listner) {
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

        RowNewsChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_news_child_home_tab, parent, false);
        return new NewsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail headWiseCollectionModel = list.get(position);
        holder.bind(headWiseCollectionModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowNewsChildHomeTabBinding itemBinding;

        MyViewHolder(RowNewsChildHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabItemDetail model) {
            itemBinding.setViewModel(model);

            itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.showFullDesc(model.getDescription(), model.getSubtitle(), model.getStratDate());
                }
            });
        }
    }

    public interface NewsInterface {
        void showFullDesc(String description, String title, String stratDate);
    }
}