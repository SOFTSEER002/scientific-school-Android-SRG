package com.jeannypr.scientificstudy.Exam.adapter;

/*
 * Author : Babulal
 * Date :
 * AbsentReportExamAdapter
 */

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Exam.model.AbsentExamModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowAbsentReportExamBinding;

import java.util.List;

public class AbsentReportExamAdapter extends RecyclerView.Adapter<AbsentReportExamAdapter.MyViewHolder> {

    Context mContext;
    private List<AbsentExamModel> absentExamList;
    String SubjectName, testName;

    @Override

    public int getItemCount() {
        return absentExamList.size();
    }

    public AbsentReportExamAdapter(Context context, List<AbsentExamModel> absentExamData, String subjectName, String test) {
        super();
        mContext = context;
        absentExamList = absentExamData;
        SubjectName = subjectName;
        testName = test;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view;

        RowAbsentReportExamBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_absent_report_exam, parent, false);
        return new AbsentReportExamAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AbsentExamModel absentExamReportModel = absentExamList.get(position);
        ((MyViewHolder) holder).bind(absentExamReportModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowAbsentReportExamBinding itemBinding;

        MyViewHolder(RowAbsentReportExamBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;


           /* if (SubjectName != null && !SubjectName.equals("")) {
                itemBinding.subjectName.setVisibility(View.GONE);

            } else {
                itemBinding.subjectName.setVisibility(View.VISIBLE);
            }*/

            if (testName != null && !testName.equals("")) {
                itemBinding.testName.setVisibility(View.GONE);

            } else {
                itemBinding.testName.setVisibility(View.VISIBLE);
            }
        }

        void bind(final AbsentExamModel model) {
            itemBinding.setAbsent(model);

            if (model.getStudentName() != null && !model.getStudentName().equals("")) {
                SetImg(model.getStudentName().charAt(0));

            } else {
                itemBinding.firstLetter.setText("");
            }

            itemBinding.rollNo.setText("Roll No.- " + model.getRollNo());

            if (model.getNotes() != null && !model.getNotes().equals("")) {
                itemBinding.notesLbl.setVisibility(View.VISIBLE);
                itemBinding.notesLbl.setText("Reason - " + model.getNotes());

            } else {
                itemBinding.notesLbl.setVisibility(View.GONE);
            }


           /* if (model.getNotes() == null && model.getNotes().equals("")) {
                itemBinding.notesLbl.setVisibility(View.GONE);

            } else {
                itemBinding.notesLbl.setVisibility(View.VISIBLE);
            }*/
        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.studentImg.getBackground();
            int colorId = Utility.GetRandomMaterialColor("materialColor", mContext);

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(colorId);

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(colorId);

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(colorId);
            }
        }
    }


}


