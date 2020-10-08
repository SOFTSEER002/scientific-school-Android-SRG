package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowStudentListForDriverBinding;

import java.util.List;

public class StudentListForDriverAdapter extends RecyclerView.Adapter<StudentListForDriverAdapter.MyViewHolder> {
    private Context mContext;
    private List<RouteDetailModel> studentList;

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public StudentListForDriverAdapter(Context context, List<RouteDetailModel> students) {
        super();
        mContext = context;
        studentList = students;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowStudentListForDriverBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_student_list_for_driver, parent, false);
        return new StudentListForDriverAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RouteDetailModel routeDetailModel = studentList.get(position);
        (holder).bind(routeDetailModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowStudentListForDriverBinding itemBinding;
        ConstraintLayout parent;

        MyViewHolder(RowStudentListForDriverBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final RouteDetailModel model) {
            itemBinding.setStudent(model);
            SetImg(model.StudentName.charAt(0));

            if (!model.getFatherMobile().equals("")) {
                itemBinding.callBtn.setVisibility(View.VISIBLE);
            } else {
                itemBinding.callBtn.setVisibility(View.GONE);
            }

            itemBinding.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                    dialPadIntent.setData(Uri.parse("tel:" + model.getFatherMobile()));
                    mContext.startActivity(dialPadIntent);
                }
            });
        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.driverProfilePic.getBackground();

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));
            }
        }
    }
}