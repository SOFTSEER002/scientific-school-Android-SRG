package com.jeannypr.scientificstudy.Fee.adapter;


import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import com.google.android.material.button.MaterialButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Fee.model.ClassWiseDuesModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowClassWiseDuesReportBinding;

import java.util.List;

public class ClassWiseDuesReportAdapter extends RecyclerView.Adapter<ClassWiseDuesReportAdapter.MyViewHolder> {

    Context mContext;
    private List<ClassWiseDuesModel> duesFeeList;
    OnItemClickListener listner;
    Boolean canSeeContactNo;

    @Override

    public int getItemCount() {
        return duesFeeList.size();
    }

    public ClassWiseDuesReportAdapter(Context context, List<ClassWiseDuesModel> dueFeedata, Boolean canSeeContactNo, OnItemClickListener mListner) {
        super();
        mContext = context;
        duesFeeList = dueFeedata;
        this.listner = mListner;
        this.canSeeContactNo = canSeeContactNo;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowClassWiseDuesReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_class_wise_dues_report, parent, false);
        return new ClassWiseDuesReportAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassWiseDuesModel ledgerReportModel = duesFeeList.get(position);
        ((MyViewHolder) holder).bind(ledgerReportModel, listner);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowClassWiseDuesReportBinding itemBinding;


        MyViewHolder(RowClassWiseDuesReportBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ClassWiseDuesModel model, final OnItemClickListener listener) {
            itemBinding.setFee(model);
       /*     itemBinding.amountVal.setText("Rs." + " " + (String.valueOf(model.Amount)));
            itemBinding.addmissionNo.setText(String.valueOf(model.AdmissionNumber));*/

            if (getAdapterPosition() % 2 == 0) {
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_purple_bg));
            } else {
                itemBinding.rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
            }

            itemBinding.studentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStudentClick(model);
                }
            });
            setListnerForCall(model.getPhone(), itemBinding.callBtn);
        }
    }

    private void setListnerForCall(final String phone, MaterialButton callBtn) {
        if (phone.equals("") || !canSeeContactNo) {
            callBtn.setVisibility(View.GONE);
        } else {
            callBtn.setVisibility(View.VISIBLE);
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                    dialPadIntent.setData(Uri.parse("tel:" + phone));
                    mContext.startActivity(dialPadIntent);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onStudentClick(ClassWiseDuesModel studentModel);
    }

}


