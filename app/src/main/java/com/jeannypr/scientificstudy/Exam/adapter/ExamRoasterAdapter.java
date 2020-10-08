package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Exam.model.ExamRoasterModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowExamRoasterBinding;


import java.util.List;

public class ExamRoasterAdapter extends RecyclerView.Adapter {

    private List<ExamRoasterModel> dataSet;
    Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowExamRoasterBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_exam_roaster, parent, false);
        return new ExamRoasterAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExamRoasterModel examListModel = dataSet.get(position);

        ((ExamRoasterAdapter.ViewHolder) holder).bind(examListModel);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public ExamRoasterAdapter(Context context, List<ExamRoasterModel> data) {
        super();
        this.dataSet = data;
        this.mContext = context;
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowExamRoasterBinding itemBinding;
        int redColorId = mContext.getResources().getColor(R.color.red);
        int colorId2 = mContext.getResources().getColor(R.color.colorPrimary);

        public ViewHolder(RowExamRoasterBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final ExamRoasterModel dataModel) {
            itemBinding.setRoaster(dataModel);

            if (dataModel.ExamDate != null && !dataModel.ExamDate.equals("")) {
                itemBinding.examDate.setText(dataModel.ExamDate);
            } else {
                itemBinding.examDate.setText("");
            }


            if (dataModel.IsPresent) {
                if (dataModel.IsGrade) {
                    itemBinding.passMarksLbl.setVisibility(View.GONE);
                    itemBinding.passMarks.setVisibility(View.GONE);
                    itemBinding.fullMarksLbl.setVisibility(View.GONE);
                    itemBinding.fullMarks.setVisibility(View.GONE);

                    if (dataModel.Grade != null && !dataModel.Grade.equals("")) {
                        itemBinding.obtainedMarks.setText(dataModel.Grade);
                        itemBinding.obtainedMarks.setTextColor(colorId2);
                    } else {
                        itemBinding.obtainedMarks.setText("NA");
                        itemBinding.obtainedMarks.setTextColor(redColorId);
                    }

                } else {
                    itemBinding.passMarksLbl.setVisibility(View.VISIBLE);
                    itemBinding.passMarks.setVisibility(View.VISIBLE);
                    itemBinding.fullMarksLbl.setText("FM: ");
                    itemBinding.fullMarksLbl.setVisibility(View.VISIBLE);
                    itemBinding.fullMarks.setVisibility(View.VISIBLE);

                    if (dataModel.FullMark != null && !dataModel.FullMark.equals("")) {
                        itemBinding.fullMarks.setText(dataModel.FullMark);
                    } else {
                        itemBinding.fullMarks.setText("");
                    }

                    if (dataModel.ObtainedMarks != null && !dataModel.ObtainedMarks.equals("")) {
                        itemBinding.obtainedMarks.setText(dataModel.ObtainedMarks);
                        itemBinding.obtainedMarks.setTextColor(colorId2);
                    } else {
                        itemBinding.obtainedMarks.setText("NA");
                        itemBinding.obtainedMarks.setTextColor(redColorId);
                    }
                }
            } else {
                itemBinding.obtainedMarks.setText("AB");
                itemBinding.obtainedMarks.setTextColor(redColorId);
                itemBinding.fullMarksLbl.setText("Notes: ");

                if (dataModel.AbsentNotes != null && !dataModel.AbsentNotes.equals("")) {
                    itemBinding.fullMarks.setText(dataModel.AbsentNotes);
                } else {
                    itemBinding.fullMarks.setText("");
                }
                itemBinding.passMarksLbl.setVisibility(View.GONE);
                itemBinding.passMarks.setVisibility(View.GONE);

            }


        }
    }

}