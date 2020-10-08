package com.jeannypr.scientificstudy.Inventory.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Inventory.model.DayBookModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowDaybookReportBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DayBookAdapter extends RecyclerView.Adapter<DayBookAdapter.MyViewHolder> {

    Context mContext;
    private List<DayBookModel> transcationList;


    @Override

    public int getItemCount() {
        return transcationList.size();
    }

    public DayBookAdapter(Context context, List<DayBookModel> transcationData) {
        super();
        mContext = context;
        transcationList = transcationData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view;

        RowDaybookReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_daybook_report, parent, false);
        return new DayBookAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DayBookModel dayBookModel = transcationList.get(position);
        ((MyViewHolder) holder).bind(dayBookModel);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowDaybookReportBinding itemBinding;

        MyViewHolder(RowDaybookReportBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final DayBookModel model) {
            itemBinding.setTranscation(model);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date dateObj = new SimpleDateFormat("dd/MM/yyyy").parse(model.Date);
                model.Date = df.format(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemBinding.transcationDate.setText(model.Date);
            if (model.Note == null || model.Note.equals("")) {
                itemBinding.narrationLbl.setVisibility(View.GONE);
            } else {
                itemBinding.narrationLbl.setVisibility(View.VISIBLE);
            }

            if (model.Credit > 0) {
                itemBinding.txtAmount.setText("Cr.-"+" ");
                itemBinding.amountTranscationCollection.setText("Rs." + " " + (String.valueOf(model.Credit)));
            } else {
                itemBinding.txtAmount.setText("Dr.-"+" ");
                itemBinding.amountTranscationCollection.setText("Rs." + " " + (String.valueOf(model.Debit)));
            }

        }

    }


}


