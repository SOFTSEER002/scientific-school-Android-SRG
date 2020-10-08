package com.jeannypr.scientificstudy.Class.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowClassListBinding;

import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter {

    private ArrayList<ClassModel> dataSet, filterData;
    private Context mContext;
    private ClassListAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowClassListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_class_list, parent, false);
        return new ClassListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ClassModel classModel = dataSet.get(position);
        classModel.adapterPosition = position;

        ((ClassListAdapter.ViewHolder) holder).bind(classModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public ClassListAdapter(Context context, ArrayList<ClassModel> data, ClassListAdapter.OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
        this.filterData = data;
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {

                    ArrayList<ClassModel> tempData = new ArrayList<>();

                    for (ClassModel row : filterData) {

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
                dataSet = (ArrayList<ClassModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RowClassListBinding itemBinding;
        private ClassModel classModel;

        ViewHolder(RowClassListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(final ClassModel dataModel, final ClassListAdapter.OnItemClickListener listener) {
            //sets the model for the binding
            itemBinding.setTeacher(dataModel);
            this.classModel = dataModel;

            if (dataModel.ClassTeacherImage != null && !dataModel.ClassTeacherImage.equals("")) {
                String path = Constants.IMAGE_BASE_URL + dataModel.ClassTeacherImage;
                Glide.with(itemBinding.getRoot()).load(path).into(itemBinding.classTeacherImg);
                itemBinding.firstLetter.setText("");
            } else {
                //  Glide.with(itemBinding.getRoot()).clear(itemBinding.teacherImg);
                // Glide.with(itemBinding.getRoot()).load(R.drawable.profile).into(itemBinding.classTeacherImg);
                if (!dataModel.Name.equals("")) {
                    SetImg(dataModel.Name.charAt(0));

                } else
                    itemBinding.firstLetter.setText("");
                itemBinding.classTeacherImg.setImageDrawable(null);
            }

            itemBinding.rowClass.setOnClickListener(this);
            itemBinding.imgRow.setOnClickListener(this);

            String teacherName;
            if (dataModel.getClassTeacher().equals("")) {
                teacherName = "No class teacher !";
                //itemBinding.addTeacher.setVisibility(View.VISIBLE);
                itemBinding.noClassTeacher.setVisibility(View.VISIBLE);
                itemBinding.classTeacherName.setVisibility(View.GONE);
                itemBinding.noClassTeacher.setText(teacherName);

            } else {

                teacherName = "Class Teacher- " + dataModel.getClassTeacher();
                //  itemBinding.addTeacher.setVisibility(View.GONE);
                itemBinding.noClassTeacher.setVisibility(View.GONE);
                itemBinding.classTeacherName.setVisibility(View.VISIBLE);
                itemBinding.classTeacherName.setText(teacherName);
            }

            //   if (dataModel.adapterPosition == 0) {
            if (dataModel.getTotalNoStudents() > 1) {
                itemBinding.studentLbl.setText("Students- ");
            } else {
                itemBinding.studentLbl.setText("Student- ");
            }
            itemBinding.studentLbl.setVisibility(View.VISIBLE);
           /* } else {
                itemBinding.studentLbl.setVisibility(View.GONE);
            }*/

            if (dataModel.getClassMonitor() != null && !dataModel.getClassMonitor().equals("")) {
                itemBinding.classMonitorLbl.setText("Monitor- " + dataModel.getClassMonitor());
                itemBinding.classMonitorLbl.setVisibility(View.VISIBLE);
            } else
                itemBinding.classMonitorLbl.setVisibility(View.GONE);


            itemBinding.chatIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChatIcon(dataModel);
                }
            });

            itemBinding.tvLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onLearn(dataModel);
                }
            });
        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.classTeacherImg.getBackground();
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
               /* case R.id.classTeacherImg:
                case R.id.classTeacherLbl:
                case R.id.classTeacherName:
                   Intent profileIntent = new Intent(mContext, TeacherProfileActivity.class);
                    profileIntent.putExtra("Id",);
                    profileIntent.putExtra("teacherId",);
                    profileIntent.putExtra("profileImage","");
                    mContext.startActivity(profileIntent);
                    break;*/

                case R.id.imgRow:
                case R.id.rowClass:
                    listener.onStudentClick(classModel);
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onStudentClick(ClassModel classModel);

        void onChatIcon(ClassModel classModel);

        void onLearn(ClassModel classModel);
    }
}