package com.jeannypr.scientificstudy.Student.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowStudentLeaveHistoryBinding;
import com.jeannypr.scientificstudy.leave.model.LeaveHistoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class StudentLeaveHistoryAdapter extends RecyclerView.Adapter {

    boolean isApprover;
    Context mContext;
    private List<LeaveHistoryModel> history;
    public StudentLeaveHistoryAdapter.OnItemClickListener listener;
    SimpleDateFormat dateFormatDMY;


    public StudentLeaveHistoryAdapter(Context context, List<LeaveHistoryModel> logs) {
        super();
        mContext = context;
        history = logs;
        dateFormatDMY = new SimpleDateFormat("dd MMM, yyyy");

    }

    @Override
    public int getItemCount() {
        return history.size();
    }


    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowStudentLeaveHistoryBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_student_leave_history, parent, false);

        return new StudentLeaveHistoryAdapter.ViewHolder(binding);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LeaveHistoryModel route = history.get(position);
        route.AdapterPosition = position;

        try {
            ((ViewHolder) holder).bind(route, listener);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowStudentLeaveHistoryBinding itemBinding;
        SimpleDateFormat df, dfStr;

        ViewHolder(RowStudentLeaveHistoryBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
            df = new SimpleDateFormat("dd MMM, yyyy");
            dfStr = new SimpleDateFormat("MM/dd/yyyy");
        }

        public void bind(final LeaveHistoryModel model, final StudentLeaveHistoryAdapter.OnItemClickListener itemListener) throws ParseException {
            itemBinding.setHistory(model);

            itemBinding.sDate.setText(df.format(dfStr.parse(model.getStartDate())));
            itemBinding.eDate.setText(" - " + df.format(dfStr.parse(model.getEndDate())));

            String totaldays = model.getTotalRequestedDays().equals("1") ? model.getTotalRequestedDays() + " day" : model.getTotalRequestedDays() + " days";
            itemBinding.totalDays.setText(totaldays);


            int ww = Math.round(itemBinding.totalDays.getPaint().measureText(totaldays));
            itemBinding.totalDaysDivider.getLayoutParams().width = ww;
            itemBinding.totalDaysDivider.setVisibility(View.VISIBLE);
        }
    }

    public interface OnItemClickListener {
        void onClick();
    }
}