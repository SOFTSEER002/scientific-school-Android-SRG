package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Fee.model.InstallmentSummaryModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class InstallmentSummaryAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<InstallmentSummaryModel> installmentSummaryModels;
    //OnItemClickListener listener;

    public InstallmentSummaryAdapter(Context context, List<InstallmentSummaryModel> routes) {
        super();
        mContext = context;
        installmentSummaryModels = routes;

    }

    @Override
    public int getItemCount() {
        return installmentSummaryModels.size();
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
                .inflate(R.layout.row_installment_summary, parent, false);
        return new InstallmentSummaryAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final InstallmentSummaryModel route = (InstallmentSummaryModel) installmentSummaryModels.get(position);
        ((MyViewHolder) holder).bind(route);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;


        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            amount = itemView.findViewById(R.id.amount);

        }

        void bind(final InstallmentSummaryModel route) {
            title.setText(route.Title != null ? route.Title : "");
            amount.setText(route.Amount != null ? route.Amount : "");

        }
    }
}


