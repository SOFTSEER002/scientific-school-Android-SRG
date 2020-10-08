package com.jeannypr.scientificstudy.leave.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.leave.model.AvailableLeavesModel;

import java.util.List;

public class AvailableLeavesAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<AvailableLeavesModel> leaves;

    public AvailableLeavesAdapter(Context context, List<AvailableLeavesModel> availableLeaves) {
        super();
        mContext = context;
        leaves = availableLeaves;

    }

    @Override
    public int getItemCount() {
        return leaves.size();
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
                .inflate(R.layout.row_available_leaves, parent, false);
        return new AvailableLeavesAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AvailableLeavesModel route = leaves.get(position);
        ((MyViewHolder) holder).bind(route);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView leaveName, leaveRatio;
        ProgressBar pieChartFore, pieChartBg;
        ConstraintLayout pieChartRow;
        Double totalIncurred = 0.0, totalEntitled = 0.0;

        MyViewHolder(View itemView) {
            super(itemView);

            leaveName = itemView.findViewById(R.id.leaveName);
            leaveRatio = itemView.findViewById(R.id.leaveRatio);
            pieChartFore = itemView.findViewById(R.id.stats_progressbar);
            pieChartBg = itemView.findViewById(R.id.background_progressbar);
            pieChartRow = itemView.findViewById(R.id.pieChartRow);
        }

        void bind(final AvailableLeavesModel leave) {
            leaveName.setText(leave.getLeaveType());
            leaveRatio.setText(leave.getConsumedLeaves() + "/" + leave.getTotalLeaves());

            int totalLeaves = (int) Math.round(leave.getTotalLeaves());
            int consumedLeaves = (int) Math.round(leave.getConsumedLeaves());

            int percent = (consumedLeaves * 100) / totalLeaves;
          /*  if (percent > 100) {
                pieChartRow.setVisibility(View.GONE);
            } else {*/
                pieChartFore.setProgress(percent);
          //  }
        }
    }
}


