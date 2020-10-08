package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Exam.model.ExamListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowExamBinding;

import java.util.List;

public class ExamListAdapter extends RecyclerView.Adapter {

    private List<ExamListModel> dataSet;
    Context mContext;
    ExamListAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowExamBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_exam, parent, false);
        return new ExamListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExamListModel examListModel = dataSet.get(position);

        ((ExamListAdapter.ViewHolder) holder).bind(examListModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public ExamListAdapter(Context context, List<ExamListModel> data, ExamListAdapter.OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowExamBinding itemBinding;
        int colorId = mContext.getResources().getColor(R.color.red);

        public ViewHolder(RowExamBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final ExamListModel dataModel, final ExamListAdapter.OnItemClickListener listener) {
            itemBinding.setExam(dataModel);

            if (dataModel.StartDate.equals("") || dataModel.StartDate == null) {
                itemBinding.from.setText("NA");
                itemBinding.from.setTextColor(colorId);
            } else {
                itemBinding.from.setText(dataModel.StartDate);
            }

            if (dataModel.EndDate.equals("") || dataModel.EndDate == null) {
                itemBinding.to.setText(" - NA");
                itemBinding.to.setTextColor(colorId);
            } else {
                itemBinding.to.setText(" - "+dataModel.EndDate);
            }

            itemBinding.examRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onExamClick(dataModel);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onExamClick(ExamListModel examListModel);
    }
}