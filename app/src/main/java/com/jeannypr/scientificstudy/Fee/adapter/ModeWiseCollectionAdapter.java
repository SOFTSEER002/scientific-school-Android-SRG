package com.jeannypr.scientificstudy.Fee.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.model.HeadWiseCollectionModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowHeadWiseCollectionBinding;

import java.util.List;

public class ModeWiseCollectionAdapter extends RecyclerView.Adapter<ModeWiseCollectionAdapter.MyViewHolder> {

    Context mContext;
    private List<HeadWiseCollectionModel> collectionModelList;


    @Override

    public int getItemCount() {
        return collectionModelList.size();
    }

    public ModeWiseCollectionAdapter(Context context, List<HeadWiseCollectionModel> collectionData) {
        super();
        mContext = context;
        collectionModelList = collectionData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowHeadWiseCollectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_head_wise_collection, parent, false);
        return new ModeWiseCollectionAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HeadWiseCollectionModel headWiseCollectionModel = collectionModelList.get(position);
        // HeadWiseCollectionModel.adapterPosition = position;
        ((MyViewHolder) holder).bind(headWiseCollectionModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowHeadWiseCollectionBinding itemBinding;

        MyViewHolder(RowHeadWiseCollectionBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final HeadWiseCollectionModel model) {
            itemBinding.setHeadName(model);
            itemBinding.toltalCollection.setText(String.valueOf(model.getAmount()));
            itemBinding.collectionName.setText(model.getModeName().substring(0, 1).toUpperCase() + model.getModeName().substring(1).toLowerCase());
        }
    }
}