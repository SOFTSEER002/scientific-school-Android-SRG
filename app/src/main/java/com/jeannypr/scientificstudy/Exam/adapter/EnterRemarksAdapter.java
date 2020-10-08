package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Exam.model.StudentRemarkModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.List;

public class EnterRemarksAdapter extends RecyclerView.Adapter {

    List<StudentRemarkModel> data;
    Context mContext;
    EnterRemarksAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_enter_remark, parent, false);
        return new EnterRemarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentRemarkModel studentModel = data.get(position);

        //TODO: remove this. find why scroll creates problem here.
        holder.setIsRecyclable(false);
        ((EnterRemarksAdapter.ViewHolder) holder).bind(studentModel, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public EnterRemarksAdapter(Context context, List<StudentRemarkModel> data, EnterRemarksAdapter.OnItemClickListener listener) {
        super();
        this.data = data;
        this.mContext = context;
        this.listener = listener;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        EditText edAttendance, edRemark;
        TextView txtStudentName, roll, firstLetter;
        ImageView studentImg;

        ViewHolder(View view) {
            super(view);
            edAttendance = view.findViewById(R.id.attendance);
            txtStudentName = view.findViewById(R.id.studentName);
            roll = view.findViewById(R.id.rollno);
            edRemark = view.findViewById(R.id.remark);
            firstLetter = view.findViewById(R.id.firstLetter);
            studentImg = view.findViewById(R.id.studentImg);
        }

        public void bind(final StudentRemarkModel studentMark, final EnterRemarksAdapter.OnItemClickListener listener) {
            if (studentMark.Name != null && !studentMark.Name.equals("")) {
                SetImg(String.valueOf(studentMark.Name.charAt(0)));
            } else {
                SetImg("");
            }

            txtStudentName.setText(studentMark.Name);
            roll.setText(String.valueOf(studentMark.Roll == -1 ? "" : "Roll no.- " + studentMark.Roll));

            edRemark.setText(studentMark.Remark == null ? "" : studentMark.Remark);
            edAttendance.setText(studentMark.Attendance == null ? "" : studentMark.Attendance);

            //    edRemark.setTag(studentMark.Id);
            // edAttendance.setTag(studentMark.Id);
            edAttendance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    studentMark.Attendance = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    studentMark.Remark = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private void SetImg(String letter) {
            firstLetter.setText(letter.toUpperCase());
            Drawable d = studentImg.getBackground();
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
        void onStudentClick(StudentRemarkModel studentModel);
    }
}