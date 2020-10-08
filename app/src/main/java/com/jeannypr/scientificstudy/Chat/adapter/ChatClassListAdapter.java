package com.jeannypr.scientificstudy.Chat.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChatClassListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TEACHER = 1;
    private Context mContext;
    private List<ClassModel> mClassList, filterList;
    private UserPreference mUserPref;
    OnItemClickListener listener;

    public ChatClassListAdapter(Context context, List<ClassModel> classList, OnItemClickListener listener) {
        super();
        mContext = context;
        mClassList = classList;
        filterList = classList;
        mUserPref = UserPreference.getInstance(context);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
//    @Override
//    public int getItemViewType(int position) {
//        return  VIEW_TYPE_TEACHER;
//    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_class_list, parent, false);
        return new ClassHolder(view);
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mClassList = filterList;

                } else {
                    ArrayList<ClassModel> temp = new ArrayList<>();
                    for (ClassModel row : filterList) {

                        if (row.Name.toLowerCase().contains(charString.toLowerCase())) {
                            temp.add(row);
                        }
                    }

                    mClassList = temp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mClassList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mClassList = (ArrayList<ClassModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ClassModel classModel = mClassList.get(position);
        classModel.adapterPosition = position;
        ((ClassHolder) holder).bind(classModel, listener);
    }

    private class ClassHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView className, classTeacherName, totalStudents, firstChar, studentLbl, classMonitorLbl, noClassTeacher, totalStudentsChat, studentChatLbl, chatIc;
        ImageView classTeacherImg;
        ClassModel classModel;

        ClassHolder(View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            classTeacherName = itemView.findViewById(R.id.classTeacherName);
            totalStudents = itemView.findViewById(R.id.totalStudents);
            //  classContainer = itemView.findViewById(R.id.classContainer);
            classTeacherImg = itemView.findViewById(R.id.classTeacherImg);
            firstChar = itemView.findViewById(R.id.firstLetter);
            studentLbl = itemView.findViewById(R.id.studentLbl);
            classMonitorLbl = itemView.findViewById(R.id.classMonitorLbl);
            chatIc = itemView.findViewById(R.id.chatIc);
            noClassTeacher = itemView.findViewById(R.id.noClassTeacher);
            totalStudentsChat = itemView.findViewById(R.id.totalStudentsChat);
            studentChatLbl = itemView.findViewById(R.id.studentChatLbl);
        }

        void bind(final ClassModel classModel, final OnItemClickListener listener) {
            this.classModel = classModel;

            itemView.findViewById(R.id.tvLearn).setVisibility(View.GONE);
            chatIc.setVisibility(View.GONE);
            studentLbl.setVisibility(View.GONE);
            totalStudents.setVisibility(View.GONE);
            SetClassTeacher(classModel.getClassTeacher());

            if (classModel.Name != null || !classModel.Name.equals("")) {
                SetImg(classModel.Name.charAt(0));
            } else {
                Log.i("", "");
            }

            className.setText(classModel.Name);
           /* if (!classModel.getClassTeacher().equals("")) {
                classTeacherName.setText(classModel.getClassTeacher().substring(0, 1).toUpperCase() + classModel.getClassTeacher().substring(1).toLowerCase());
            } else {
                classTeacherName.setText("");
            }*/

            totalStudents.setText(String.valueOf(classModel.getTotalNoStudents()));
            totalStudentsChat.setVisibility(View.VISIBLE);
            totalStudentsChat.setText(String.valueOf(classModel.getTotalNoStudents()));
            if (classModel.adapterPosition == 0) {
                if (classModel.getTotalNoStudents() > 1) {
                    studentChatLbl.setText("Students ");
                } else {
                    studentChatLbl.setText("Student " + String.valueOf(classModel.getTotalNoStudents()));
                }
                studentChatLbl.setVisibility(View.VISIBLE);
            } else {
                studentChatLbl.setVisibility(View.GONE);
            }

          /*  if (classModel.adapterPosition == 0) {
                if (classModel.getTotalNoStudents() > 1) {
                    studentLbl.setText("Students");
                } else {
                    studentLbl.setText("Student");
                }
                studentLbl.setVisibility(View.VISIBLE);
            } else {
                studentLbl.setVisibility(View.GONE);
            }*/

            if (classModel.getClassMonitor() != null && !classModel.getClassMonitor().equals("")) {
                classMonitorLbl.setVisibility(View.VISIBLE);
                classMonitorLbl.setText("Monitor- " + classModel.getClassMonitor());
            } else {
                classMonitorLbl.setVisibility(View.GONE);
                classMonitorLbl.setText("");
            }

            // Glide.with(itemView).load(R.drawable.profile).into(classTeacherImg);

            /*classContainer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onClassClick(classModel);
                }
            });*/

            className.setOnClickListener(this);
            classTeacherName.setOnClickListener(this);
            classTeacherImg.setOnClickListener(this);

            totalStudents.setOnClickListener(this);
            studentLbl.setOnClickListener(this);

            totalStudentsChat.setOnClickListener(this);
        }

        private void SetClassTeacher(String classTeacher) {
            String teacherName;
            if (classTeacher.equals("")) {
                teacherName = "No class teacher !";
                noClassTeacher.setVisibility(View.VISIBLE);
                classTeacherName.setVisibility(View.GONE);
                noClassTeacher.setText(teacherName);

            } else {
                teacherName = "Class Teacher- " + classTeacher.substring(0, 1).toUpperCase() + classTeacher.substring(1).toLowerCase();
                noClassTeacher.setVisibility(View.GONE);
                classTeacherName.setVisibility(View.VISIBLE);
                classTeacherName.setText(teacherName);
            }
        }

        private void SetImg(char firstLetter) {
            try {
                firstChar.setText(String.valueOf(firstLetter).toUpperCase());
                Drawable d = classTeacherImg.getBackground();
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
            } catch (Exception ex) {
                Log.e("Chat - Class list", ex.getMessage());
            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
             /*   case R.id.studentLbl:
                case R.id.totalStudents:*/
                case R.id.studentChatLbl:
                case R.id.totalStudentsChat:
                    listener.onStudentClick(classModel);
                    break;

                case R.id.className:
                case R.id.classTeacherName:
                case R.id.classTeacherImg:
                    listener.onClassClick(classModel);
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onClassClick(ClassModel classModel);

        void onStudentClick(ClassModel classModel);
    }
}