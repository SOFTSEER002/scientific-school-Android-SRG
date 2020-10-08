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

public class MonthWiseCollectionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DateWiseCollectionModel> dateWiseCollectionModels;
    //private UserPreference mUserPref;

    public MonthWiseCollectionAdapter(Context context, List<DateWiseCollectionModel> collections) {
        super();
        mContext = context;
        dateWiseCollectionModels = collections;
        //  mUserPref = UserPreference.getInstance(context);
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
                .inflate(R.layout.row_monthwise_collection, parent, false);
        return new MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DateWiseCollectionModel message = (DateWiseCollectionModel) dateWiseCollectionModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView paymentDate, studentName, className,
                amount, admNo, paymentMode;
        ConstraintLayout rowContainer, paymentConatiner;

        MyViewHolder(View itemView) {
            super(itemView);

            rowContainer = itemView.findViewById(R.id.row_Container);
            paymentConatiner = itemView.findViewById(R.id.paymentConatiner);

            admNo = itemView.findViewById(R.id.addmissionNo);
            paymentDate = itemView.findViewById(R.id.paymentDate);
            studentName = itemView.findViewById(R.id.studentName);
            className = itemView.findViewById(R.id.className);
            amount = itemView.findViewById(R.id.payAmount);
            admNo = itemView.findViewById(R.id.addmissionNo);
            paymentMode = itemView.findViewById(R.id.paymentMode);
        }

        void bind(DateWiseCollectionModel message) {

            studentName.setText(message.StudentName != null ? message.StudentName.substring(0, 1).toUpperCase() + message.StudentName.substring(1).toLowerCase() : "");
            className.setText("Class: " + message.ClassName);
            amount.setText("Rs. " + message.Amount);
            admNo.setText("Adm no. " + message.AdmissionNumber);
            paymentDate.setText(message.PaymentDate);
            paymentMode.setText(message.PaymentMode);


            if (getAdapterPosition() % 2 == 0) {
                rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_purple_bg));
                paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_purple_bg));
            } else {
                rowContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
                paymentConatiner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.amount_mode_red_bg));
            }

           /* if (message.PaymentMode != null && !message.PaymentMode.equals("")) {
                paymentMode.setText(message.PaymentMode);
              //  RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
                //   paymentMode.setAnimation(rotate);
            } else {
                //paymentMode.setVisibility(View.GONE);
                paymentMode.setText("Nil");
            }*/


        }
    }

}


