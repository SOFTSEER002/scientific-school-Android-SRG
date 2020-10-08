package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Fee.model.ConsolidatedDuesModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class ConsolidatedAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ConsolidatedDuesModel> consolidatedDuesModels;
    private UserPreference mUserPref;


    public ConsolidatedAdapter(Context context, List<ConsolidatedDuesModel> collections) {
        super();
        mContext = context;
        consolidatedDuesModels = collections;
        mUserPref = UserPreference.getInstance(context);

    }

    @Override
    public int getItemCount() {
        return consolidatedDuesModels.size();
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
                .inflate(R.layout.row_consolidated_dues, parent, false);
        return new MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsolidatedDuesModel message = (ConsolidatedDuesModel) consolidatedDuesModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView className, feeType, expectedAmount, paidAmount, dueAmount;
        ImageView statusImg;
        View divider;

        MyViewHolder(View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            feeType = itemView.findViewById(R.id.feeType);
            expectedAmount = itemView.findViewById(R.id.expectedAmount);
            paidAmount = itemView.findViewById(R.id.paidAmount);
            dueAmount = itemView.findViewById(R.id.dueAmount);
            statusImg = itemView.findViewById(R.id.statusImg);
            divider = itemView.findViewById(R.id.divider);
        }

        void bind(ConsolidatedDuesModel message) {
            className.setText(message.getClassName());
            if (message.getFeeTitle() == "") {
                feeType.setVisibility(View.GONE);
            } else {
                feeType.setText(message.getFeeTitle());
            }

            String expected = String.valueOf(message.getExpected());
            expectedAmount.setText(expected);

            int width = expectedAmount.getWidth();
            divider.getLayoutParams().width = width;

            String paid = String.valueOf(message.getPaid());
            paidAmount.setText(paid);

            String due = String.valueOf(message.getDue());
            dueAmount.setText(due);

            if (message.getDue() == 0) {
                statusImg.setImageResource(R.drawable.paid);
            } else {
                statusImg.setImageResource(R.drawable.due);
            }


        }
    }

}


