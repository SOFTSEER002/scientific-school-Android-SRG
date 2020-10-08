package com.jeannypr.scientificstudy.Inventory.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jeannypr.scientificstudy.Inventory.model.LedgerReportModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowLedgerReportBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LedgerReportAdapter extends RecyclerView.Adapter<LedgerReportAdapter.MyViewHolder> {

    Context mContext;
    private List<LedgerReportModel> ledgerReportModelList;


    @Override

    public int getItemCount() {
        return ledgerReportModelList.size();
    }

    public LedgerReportAdapter(Context context, List<LedgerReportModel> ledgerData) {
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

        RowLedgerReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_ledger_report, parent, false);
        return new LedgerReportAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LedgerReportModel ledgerReportModel = ledgerReportModelList.get(position);
        ((MyViewHolder) holder).bind(ledgerReportModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowLedgerReportBinding itemBinding;


        MyViewHolder(RowLedgerReportBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final LedgerReportModel model) {
            itemBinding.setLedger(model);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateObj = new SimpleDateFormat("dd/MM/yyyy").parse(model.Date);
                model.Date = df.format(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemBinding.txtDate.setText(model.Date);
            if (model.Note == null || model.Note.equals("")) {
                itemBinding.narrationLbl.setVisibility(View.GONE);
            } else {
                itemBinding.narrationLbl.setVisibility(View.VISIBLE);
            }

            if (model.Credit > 0) {
                itemBinding.txtAmount.setText("Cr.-"+" ");
                itemBinding.txtAmountVal.setText("Rs." + " " + (String.valueOf(model.Credit)));
            } else {
                itemBinding.txtAmount.setText("Dr.-"+" ");
                itemBinding.txtAmountVal.setText("Rs." + " " + (String.valueOf(model.Debit)));
            }

        }

    }


}


