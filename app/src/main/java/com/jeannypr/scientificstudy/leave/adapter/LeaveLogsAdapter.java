package com.jeannypr.scientificstudy.leave.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.leave.model.LogsModel;

import java.util.List;

public class LeaveLogsAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<LogsModel> leaveLogs;
    //OnItemClickListener listener;

    public LeaveLogsAdapter(Context context, List<LogsModel> logs) {
        super();
        mContext = context;
        leaveLogs = logs;
    }

    @Override
    public int getItemCount() {
        return leaveLogs.size();
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
                .inflate(R.layout.row_leave_log, parent, false);
        return new LeaveLogsAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LogsModel route = (LogsModel) leaveLogs.get(position);
        ((MyViewHolder) holder).bind(route);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView totalLeaves;
        View divider1;

        MyViewHolder(View itemView) {
            super(itemView);

            totalLeaves = itemView.findViewById(R.id.totalLeaves);
            divider1 = itemView.findViewById(R.id.divider1);
        }

        void bind(final LogsModel route) {
            int width = totalLeaves.getWidth();
            divider1.getLayoutParams().width = width;

        }
    }
}


