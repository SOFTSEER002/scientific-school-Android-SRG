package com.jeannypr.scientificstudy.Classwork.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Classwork.model.ActivityModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowClassworkListBinding;

import java.util.ArrayList;

public class CwHwListAdapter extends RecyclerView.Adapter {

    private ArrayList<ActivityModel> dataSet;
    Context mContext;
    OnItemClickListener listener;
    Boolean isParent;
    String activityType;
    int activityTypeId;

    public CwHwListAdapter(Context context, ArrayList<ActivityModel> data, Boolean isParent, String activityType, int activityTypeId, OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
        this.isParent = isParent;
        this.activityTypeId = activityTypeId;
        this.activityType = activityType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowClassworkListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_classwork_list,
                parent, false);
        return new CwHwListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ActivityModel classworkModel = dataSet.get(position);

        ((CwHwListAdapter.ViewHolder) holder).bind(classworkModel, listener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowClassworkListBinding itemBinding;
        // RequestOptions options;

        public ViewHolder(RowClassworkListBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

//            options = new RequestOptions();
//            options.centerInside();
        }

        public void bind(final ActivityModel dataModel, final CwHwListAdapter.OnItemClickListener itemListener) {
            itemBinding.setClsWork(dataModel);
            if (dataModel.getActivityType() == Constants.DiaryType.Classwork) {
                itemBinding.activityType.setText("C.W.");
            } else {
                itemBinding.activityType.setText("H.W.");
            }

            if (!dataModel.getIsAssignedToClass()) {
                itemBinding.isAssigned.setText("Not Assigned");
                itemBinding.isAssigned.setTextColor(mContext.getResources().getColor(R.color.mRed));
            } else {
                itemBinding.isAssigned.setText("Assigned");
                itemBinding.isAssigned.setTextColor(mContext.getResources().getColor(R.color.mBlack));
            }

//            RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
//            itemBinding.activityType.setAnimation(rotate);

            if (dataModel.TeacherName == null || dataModel.TeacherName.equals("")) {
                itemBinding.teacherRow.setVisibility(View.GONE);

            } else {
                itemBinding.teacherRow.setVisibility(View.VISIBLE);
                itemBinding.teacher.setText(dataModel.TeacherName.substring(0, 1).toUpperCase() + dataModel.TeacherName.substring(1).toLowerCase());
            }
            if (dataModel.SubjectName == null || dataModel.SubjectName.equals("")) {
                itemBinding.icBook.setVisibility(View.GONE);

            }

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onClick(dataModel);
                }
            });

           /* itemBinding.icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editCwHwIntent = new Intent(mContext, CreateCwHwActivity.class);
                    editCwHwIntent.putExtra("activityId", dataModel.Id);
                    editCwHwIntent.putExtra("activityType", activityType);
                    editCwHwIntent.putExtra("activityTypeId", activityTypeId);
                    mContext.startActivity(editCwHwIntent);
                }
            });*/

            if (isParent) {
                itemBinding.isAssigned.setVisibility(View.GONE);
            }

        }


    }

    public interface OnItemClickListener {
        void onClick(ActivityModel teacherModel);
    }
}