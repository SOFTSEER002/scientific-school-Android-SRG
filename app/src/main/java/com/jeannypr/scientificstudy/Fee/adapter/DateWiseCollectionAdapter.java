package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Fee.model.DateWiseCollectionModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class DateWiseCollectionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DateWiseCollectionModel> dateWiseCollectionModels;
    // private UserPreference mUserPref;

    public DateWiseCollectionAdapter(Context context, List<DateWiseCollectionModel> collections) {
        super();
        mContext = context;
        dateWiseCollectionModels = collections;
        //   mUserPref = UserPreference.getInstance(context);
    }

    @Override
    public int getItemCount() {
        return dateWiseCollectionModels.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_datewise_collection, parent, false);
        return new MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DateWiseCollectionModel message = (DateWiseCollectionModel) dateWiseCollectionModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView paymentDate, studentName, className, amount, admNo, paymentMode;
        ConstraintLayout feeContainer, feeAmount;

        MyViewHolder(View itemView) {
            super(itemView);

            paymentDate = itemView.findViewById(R.id.paymentDate);
            studentName = itemView.findViewById(R.id.studentName);
            className = itemView.findViewById(R.id.className);
            amount = itemView.findViewById(R.id.amount);
            admNo = itemView.findViewById(R.id.admNo);
            paymentMode = itemView.findViewById(R.id.mode);
            feeAmount = itemView.findViewById(R.id.feeAmount);
            feeContainer = itemView.findViewById(R.id.feeContainer);
        }

        void bind(DateWiseCollectionModel message) {
            if (getAdapterPosition() % 2 == 0) {
                feeAmount.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_purple_bg));
                feeContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_purple_bg));
            } else {
                feeAmount.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
                feeContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
            }
            studentName.setText(message.StudentName);
            className.setText("Class: " + message.ClassName);

            amount.setText("Rs. " + message.Amount);
            admNo.setText("Adm no.: " + message.AdmissionNumber);
            paymentMode.setText(message.PaymentMode);
            paymentDate.setText(message.PaymentDate);
            /*RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
            paymentMode.setAnimation(rotate);*/


        }
    }
}


