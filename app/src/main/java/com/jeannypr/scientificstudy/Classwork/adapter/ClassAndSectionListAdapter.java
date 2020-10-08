package com.jeannypr.scientificstudy.Classwork.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.ClassAndSectionsModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowClassWithSectionsBinding;

import java.util.ArrayList;
import java.util.List;

public class ClassAndSectionListAdapter extends RecyclerView.Adapter {

    private ArrayList<ClassAndSectionsModel> dataSet;
    Context mContext;
    private int colorId;
    ClassAndSectionListAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowClassWithSectionsBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_class_with_sections, parent, false);
      /*  ConstraintLayout classRow = binding.classAndSectionRow;

        CheckBox section = new CheckBox(mContext);
        ViewGroup.LayoutParams params = section.getLayoutParams();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        section.setText("checkbox1");
        section.setLayoutParams(params);

        classRow.addView(section);*/
        return new ClassAndSectionListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ClassAndSectionsModel classModel = dataSet.get(position);

        ((ClassAndSectionListAdapter.ViewHolder) holder).bind(classModel, listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public ClassAndSectionListAdapter(Context context, ArrayList<ClassAndSectionsModel> data, ClassAndSectionListAdapter.OnItemClickListener listener) {
        super();
        this.dataSet = data;
        this.mContext = context;
        colorId = mContext.getResources().getColor(R.color.green2);
        this.listener = listener;
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        public RowClassWithSectionsBinding itemBinding;
        int colorPrimaryVal, greyColorVal, whiteColorVal;
        Boolean isSelected;
        ConstraintLayout classRow;
        LinearLayout sectionsRow;
        List<ClassAndSectionsModel> classAndSectionsModels;

        public ViewHolder(RowClassWithSectionsBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

            colorPrimaryVal = mContext.getResources().getColor(R.color.colorPrimary);
            greyColorVal = mContext.getResources().getColor(R.color.black3);
            whiteColorVal = mContext.getResources().getColor(R.color.white);
            classRow = itemBinding.classAndSectionRow;
            sectionsRow = itemBinding.sectionsRow;


        }

        public void bind(final ClassAndSectionsModel dataModel, final OnItemClickListener listener) {

            //sets the model for the binding
            itemBinding.setCls(dataModel);
            isSelected = false;

            //create view for sections
            if (dataModel != null && dataModel.Sections.size() > 0) {
                for (final ClassAndSectionsModel section : dataModel.Sections) {
                    CreateView(section);
                }
            }else {
                return;
            }

            itemBinding.selectClassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelected = !isSelected;

                    if (isSelected) {
                        itemBinding.selectClassBtn.setImageResource(R.drawable.present);
                        itemBinding.sectionsRow.setVisibility(View.VISIBLE);

                        //hide other rows

                    } else {
                        itemBinding.selectClassBtn.setImageResource(R.drawable.tickmark);
                        itemBinding.sectionsRow.setVisibility(View.GONE);
                    }
                }
            });
        }

        public void CreateView(final ClassAndSectionsModel section) {
            //Create view
            final TextView sectionChk = new TextView(mContext);

            //set width and height of view
            ViewGroup.LayoutParams params = sectionChk.getLayoutParams();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            //set other attr
            sectionChk.setText(section.SectionName);
            sectionChk.setTag(section.SectionName);
            sectionChk.setPadding(10, 4, 10, 4);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                sectionChk.setBackground(mContext.getResources().getDrawable(R.drawable.white_bg_blue_border));
            }

            //set click event
            sectionChk.setClickable(true);
            sectionChk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onClassClick(section);

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(colorPrimaryVal);
                    gd.setCornerRadius(98.0f);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        sectionChk.setBackground(gd);
                    }

                    sectionChk.setTextColor(whiteColorVal);
                }
            });

            sectionChk.setLayoutParams(params);
            sectionsRow.addView(sectionChk);

            //set margin
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) sectionChk.getLayoutParams();
            params1.setMargins(0, 2, 0, 2);
            sectionChk.setLayoutParams(params1);
        }
    }

    public interface OnItemClickListener {
        void onClassClick(ClassAndSectionsModel classModel);
    }
}