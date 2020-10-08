package com.jeannypr.scientificstudy.Teacher.adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowTeacherNewBinding;

import java.util.ArrayList;

public class TeacherListAdapter extends RecyclerView.Adapter {

    private ArrayList<TeacherModel> dataSet, filterData;
    Context mContext;
    TeacherListAdapter.OnItemClickListener listener;

    public TeacherListAdapter(Context context, ArrayList<TeacherModel> data, TeacherListAdapter.OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        this.listener = listener;
        this.filterData = data;
    }

    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {
                    ArrayList<TeacherModel> tempData = new ArrayList<>();

                    for (TeacherModel row : filterData) {

                        if (row.Name.toLowerCase().contains(charString.toLowerCase()))
                            tempData.add(row);
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
                dataSet = (ArrayList<TeacherModel>) filterResults.values;
                notifyDataSetChanged();

                if (dataSet.size() > 0) listener.showSearchMsg(true);
                else listener.showSearchMsg(false);
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowTeacherNewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_teacher_new, parent, false);

        return new TeacherListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeacherModel teacherModel = dataSet.get(position);

        ((TeacherListAdapter.ViewHolder) holder).bind(teacherModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowTeacherNewBinding itemBinding;
        RequestOptions options;

        public ViewHolder(RowTeacherNewBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
            options = new RequestOptions();
            options.centerInside();
        }

        public void bind(final TeacherModel dataModel, final TeacherListAdapter.OnItemClickListener itemListener) {

            itemBinding.setTeacher(dataModel);
            itemBinding.teacherName.setText(dataModel.Name.substring(0, 1).toUpperCase() + dataModel.Name.substring(1).toLowerCase());

            if (dataModel.ClassName != null && !dataModel.ClassName.equals("")) {
                itemBinding.className.setVisibility(View.VISIBLE);

                if (getAdapterPosition() == 0) {
                    itemBinding.className.setText("class teacher of " + dataModel.ClassName);
                } else {
                    itemBinding.className.setText(dataModel.ClassName);
                }
            } else {
                itemBinding.className.setVisibility(View.GONE);
            }

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true);

            if (dataModel.ImagePath != null && !dataModel.ImagePath.equals("")) {
                String path = Constants.IMAGE_BASE_URL + dataModel.ImagePath;
                Glide.with(itemBinding.getRoot()).load(path).apply(requestOptions).into(itemBinding.teacherImg);
                itemBinding.firstLetter.setText("");
            } else {
                //Glide.with(itemBinding.getRoot()).load(R.drawable.profile).into(itemBinding.teacherImg);
                if (!dataModel.Name.equals("") || dataModel.Name != null) {
                    itemBinding.teacherImg.setImageDrawable(null);
                    SetImg(dataModel.Name.charAt(0));
                } else {
                    Log.e("", "");
                }
            }

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onClick(dataModel);
                }
            });

            if (dataModel.Email != null && !dataModel.Email.equals("")) {
                itemBinding.teacherEmail.setVisibility(View.VISIBLE);

            } else {
                itemBinding.teacherEmail.setVisibility(View.GONE);
            }

            itemBinding.teacherEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mailId = dataModel.Email.trim();
                    if (mailId != "") {
                        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mailId));
                        mContext.startActivity(emailIntent);
                    }
                }
            });

            if (dataModel.Mobile != null && !dataModel.Mobile.equals("")) {
                itemBinding.callBtn.setVisibility(View.VISIBLE);

            } else {
                itemBinding.callBtn.setVisibility(View.GONE);
            }

            itemBinding.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                    dialPadIntent.setData(Uri.parse("tel:" + dataModel.Mobile));
                    mContext.startActivity(dialPadIntent);
                }
            });

            itemBinding.chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ChatBtnClick(dataModel);
                }
            });
        }

        private void SetImg(char firstLetter) {
            itemBinding.firstLetter.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = itemBinding.teacherImg.getBackground();
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
        void onClick(TeacherModel teacherModel);

        void ChatBtnClick(TeacherModel teacherModel);

        void showSearchMsg(Boolean isDataFound);
    }
}