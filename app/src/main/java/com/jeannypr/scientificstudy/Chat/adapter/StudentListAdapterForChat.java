package com.jeannypr.scientificstudy.Chat.adapter;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowStudentListBinding;

import java.util.ArrayList;

public class StudentListAdapterForChat extends RecyclerView.Adapter {

    private ArrayList<StudentModel> dataSet, filterData;
    private Context mContext;
    private StudentListAdapterForChat.OnItemClickListener listener;
    private String role;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowStudentListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_student_list, parent, false);
        return new StudentListAdapterForChat.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentModel studentModel = dataSet.get(position);
        ((StudentListAdapterForChat.ViewHolder) holder).bind(studentModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public StudentListAdapterForChat(Context context, ArrayList<StudentModel> data, String role, StudentListAdapterForChat.OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
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

                } else {
                    dataSet = filterData;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSet;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataSet = (ArrayList<StudentModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowStudentListBinding itemBinding;

        ViewHolder(RowStudentListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final StudentModel studentModel, final StudentListAdapterForChat.OnItemClickListener listener) {
            itemBinding.setStudent(studentModel);

            itemBinding.chatBtn.setVisibility(View.GONE);
            if (studentModel.Name != null && !studentModel.Name.equals("")) {
                SetImg(studentModel.Name.charAt(0));
            } else {
                Log.e("", "");
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


            setContact(studentModel.PhoneNo, studentModel.getCanSeeContactNumber());

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

        private void setContact(final String phoneNo, Boolean canSeeContactNumber) {
            if (role.equals(Constants.Role.ADMIN)) {
                canSeeContactNumber = true;
            }
            if (phoneNo != null && !phoneNo.equals("") && canSeeContactNumber) {
                itemBinding.callBtn.setVisibility(View.VISIBLE);

                itemBinding.callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                        dialPadIntent.setData(Uri.parse("tel:" + phoneNo));
                        mContext.startActivity(dialPadIntent);
                    }
                });
            } else {
                itemBinding.callBtn.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onStudentClick(StudentModel studentModel);

        void onChatBtnClick(StudentModel studentModel);
    }
}