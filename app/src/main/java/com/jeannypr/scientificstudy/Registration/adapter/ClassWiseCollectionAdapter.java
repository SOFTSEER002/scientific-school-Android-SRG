package com.jeannypr.scientificstudy.Registration.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.LedgerReportModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowClassWiseCollectionBinding;

import java.util.List;

public class ClassWiseCollectionAdapter extends RecyclerView.Adapter<ClassWiseCollectionAdapter.MyViewHolder> {

    Context mContext;
    private List<LedgerReportModel> ledgerReportModelList;


    @Override

    public int getItemCount() {
        return ledgerReportModelList.size();
    }

    public ClassWiseCollectionAdapter(Context context, List<LedgerReportModel> ledgerData) {
        super();
        mContext = context;
        ledgerReportModelList = ledgerData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowClassWiseCollectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_class_wise_collection, parent, false);
        return new ClassWiseCollectionAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LedgerReportModel ledgerReportModel = ledgerReportModelList.get(position);
        ((MyViewHolder) holder).bind(ledgerReportModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowClassWiseCollectionBinding itemBinding;


        MyViewHolder(RowClassWiseCollectionBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final LedgerReportModel model) {
            // itemBinding.setLedger(model);

        }

    }


}


