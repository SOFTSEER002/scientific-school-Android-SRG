package com.jeannypr.scientificstudy.Student.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowStudentListBinding;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter {

    private ArrayList<StudentModel> dataSet, filterData;
    Context mContext;
    private int colorId;
    StudentAdapter.OnItemClickListener listener;
    String role;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowStudentListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_student_list, parent, false);
        return new StudentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentModel studentModel = dataSet.get(position);

        ((StudentAdapter.ViewHolder) holder).bind(studentModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public StudentAdapter(Context context, ArrayList<StudentModel> data, String role, OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        colorId = mContext.getResources().getColor(R.color.green2);
        this.listener = listener;
        this.filterData = data;
        this.role = role;
    }

    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {
                    ArrayList<StudentModel> tempData = new ArrayList<>();

                    for (StudentModel row : filterData) {
                        if (row.Name.toLowerCase().contains(charString.toLowerCase())) {
                            tempData.add(row);
                        }
                    }
                    dataSet = tempData;

                } else
                    dataSet = filterData;


                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSet;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataSet = (ArrayList<StudentModel>) filterResults.values;
                notifyDataSetChanged();

                if (dataSet.size() > 0) listener.showSearchMsg(true);
                else listener.showSearchMsg(false);
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowStudentListBinding itemBinding;

        ViewHolder(RowStudentListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final StudentModel studentModel, final StudentAdapter.OnItemClickListener listener) {
            itemBinding.setStudent(studentModel);

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true);

            if (studentModel != null && studentModel.ImagePath != null && !studentModel.ImagePath.equals("")) {
                String imgPath = Constants.IMAGE_BASE_URL + studentModel.ImagePath;
                Glide.with(mContext).load(imgPath).apply(requestOptions).into(itemBinding.studentImg);
                itemBinding.firstLetter.setText("");

            } else {
                itemBinding.studentImg.setImageDrawable(null);
                if (studentModel.Name != null && !studentModel.Name.equals("")) {
                    SetImg(String.valueOf(studentModel.Name.charAt(0)));
                } else {
                    SetImg("");
                }
            }


            if (studentModel.AdmissionNo != null && !studentModel.AdmissionNo.equals("")) {
                itemBinding.studentAdmNo.setVisibility(View.VISIBLE);
                itemBinding.studentAdmNo.setText("Admission No.-" + " " + studentModel.AdmissionNo);

            } else {
                itemBinding.studentAdmNo.setVisibility(View.GONE);
            }

            if (studentModel.RollNo != null && !studentModel.RollNo.equals("")) {
                itemBinding.studentRoll.setVisibility(View.VISIBLE);
                itemBinding.studentRoll.setText("Roll No.-" + " " + studentModel.RollNo);

            } else {
                itemBinding.studentRoll.setVisibility(View.GONE);
            }

            itemBinding.rowClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStudentClick(studentModel);
                }
            });

            Boolean canSeeContactNo = studentModel.getCanSeeContactNumber();
            if (role.equals(Constants.Role.ADMIN)) {
                canSeeContactNo = true;
            }
            if (studentModel.PhoneNo != null && !studentModel.PhoneNo.equals("") && canSeeContactNo) {
                itemBinding.callBtn.setVisibility(View.VISIBLE);
            } else {
                itemBinding.callBtn.setVisibility(View.GONE);
            }

            itemBinding.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                    dialPadIntent.setData(Uri.parse("tel:" + studentModel.PhoneNo));
                    mContext.startActivity(dialPadIntent);
                }
            });

            if (studentModel.EmailId != null && !studentModel.EmailId.equals("")) {
                itemBinding.emailBtn.setVisibility(View.VISIBLE);
            } else {
                itemBinding.emailBtn.setVisibility(View.GONE);
            }

            itemBinding.emailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + studentModel.EmailId));
                    mContext.startActivity(emailIntent);
                }
            });

            itemBinding.chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChatBtnClick(studentModel);
                }
            });
        }

        private void SetImg(String firstLetter) {
            itemBinding.firstLetter.setText(firstLetter.toUpperCase());
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

    public interface OnItemClickListener {
        void onStudentClick(StudentModel studentModel);

        void onChatBtnClick(StudentModel studentModel);

        void showSearchMsg(Boolean isDataFound);
    }
}