package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Fee.model.FeeSummaryModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class DiscountStudentDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<FeeSummaryModel> collectionModels;


    public DiscountStudentDetailAdapter(Context context, List<FeeSummaryModel> collections) {
        super();
        mContext = context;
        collectionModels = collections;
    }

    @Override
    public int getItemCount() {
        return collectionModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_discount, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeeSummaryModel message = (FeeSummaryModel) collectionModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView installmentDate, dueDate, installmentTitle, payableAmount, paidAmount, dueAmount, installmentStatus;
        ConstraintLayout rowContainer, paymentConatiner;

        MyViewHolder(View itemView) {
            super(itemView);

            installmentTitle = itemView.findViewById(R.id.installmentTitle);
            payableAmount = itemView.findViewById(R.id.payableAmount);
            paidAmount = itemView.findViewById(R.id.paidAmount);
            dueAmount = itemView.findViewById(R.id.dueAmount);
            installmentDate = itemView.findViewById(R.id.installmentDate);
            installmentStatus = itemView.findViewById(R.id.installmentStatus);

            rowContainer = itemView.findViewById(R.id.row_Container);
            paymentConatiner = itemView.findViewById(R.id.paymentConatiner);


        }

        void bind(FeeSummaryModel message) {
            String totalPayable = message.TotalAmountPayable.toString();
            String totalPaid = message.TotalAmountPaid.toString();
            String totalDues = message.TotalAmountDue.toString();

            installmentTitle.setText(message.InstallmentTitle == null ? "" : message.InstallmentTitle);
            payableAmount.setText("Payable: " + (message.TotalAmountPayable == null ? "0" : totalPayable));
            paidAmount.setText("Paid: " + (message.TotalAmountPaid == null ? "0" : totalPaid));
            dueAmount.setText("Due: " + (message.TotalAmountDue == null ? "0" : totalDues));

            String paymentStatus = message.PaymentStatus;
            if (paymentStatus.equals(Constants.PaymentStatus.PAID)) {
                rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.due_collection_green_bg));
                paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.due_row_green_bg));
                installmentDate.setText(message.InstallmentDate == null ? "" : message.InstallmentDate);
                installmentStatus.setText("PAID");

            } else if (paymentStatus.equals(Constants.PaymentStatus.DUE)) {
                installmentStatus.setText("DUE");
                rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
                installmentDate.setText(message.DueDate == null ? "" : message.DueDate);

            } else {
                installmentStatus.setText("OVERDUE");
                rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
                installmentDate.setText(message.DueDate == null ? "" : message.DueDate);
            }

        }
    }
}


