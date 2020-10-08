package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowFamilyHomeTabBottomBinding;
import com.jeannypr.scientificstudy.databinding.RowFamilyHomeTabTopBinding;

import java.util.List;

public class FamilyMembersAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChildModel> list;
    private FamilyMembersAdapter.FamilyMembersInterface mListner;
    private static final int TOP_VIEW = 1;
    private static final int BOTTOM_VIEW = 2;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public FamilyMembersAdapter(Context context, List<ChildModel> data, FamilyMembersInterface listner) {
        super();
        mContext = context;
        list = data;
        this.mListner = listner;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) return TOP_VIEW;
        else return BOTTOM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TOP_VIEW) {
            return new TopGravityRowVH((RowFamilyHomeTabTopBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_family_home_tab_top, parent, false));

        } else {
            return new BottomGravityRowVH((RowFamilyHomeTabBottomBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_family_home_tab_bottom, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ChildModel model = list.get(position);
        model.adapterPosition = position;

        if (holder.getItemViewType() == TOP_VIEW)
            ((TopGravityRowVH) holder).bind(model);
        else
            ((BottomGravityRowVH) holder).bind(model);
    }

    class TopGravityRowVH extends ViewHolder {

        public RowFamilyHomeTabTopBinding itemBinding;

        TopGravityRowVH(RowFamilyHomeTabTopBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ChildModel model) {
            itemBinding.setViewModel(model);

            if (model.getStudentImagePath().equals("")) {
                if (model.getGender().equals(Constants.Gender.MALE)) {
                    itemBinding.profileImg.setImageResource(R.mipmap.profile_md);
                } else {
                    itemBinding.profileImg.setImageResource(R.mipmap.girl_ic);
                }

            } else {
                Glide.with(mContext).load(Constants.IMAGE_BASE_URL + model.getStudentImagePath()).into(itemBinding.profileImg);
            }

            if (model.getGender().equals(Constants.Gender.MALE)) {
                itemBinding.heartIc.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.purple4)));
            } else {
                itemBinding.heartIc.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.red)));
            }

            itemBinding.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.onClickChildRow(model);
                }
            });
        }
    }

    class BottomGravityRowVH extends ViewHolder {

        public RowFamilyHomeTabBottomBinding itemBinding;

        BottomGravityRowVH(RowFamilyHomeTabBottomBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ChildModel model) {
            itemBinding.setViewModel(model);

            if (model.getStudentImagePath().equals("")) {
                if (model.getGender().equals(Constants.Gender.MALE)) {
                    itemBinding.profileImg.setImageResource(R.mipmap.profile_md);
                } else {
                    itemBinding.profileImg.setImageResource(R.mipmap.girl_ic);
                }

            } else {
                Glide.with(mContext).load(Constants.IMAGE_BASE_URL + model.getStudentImagePath()).into(itemBinding.profileImg);
            }

            if (model.getGender().equals(Constants.Gender.MALE)) {
                itemBinding.heartIc.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.purple4)));
            } else {
                itemBinding.heartIc.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.red)));
            }

            itemBinding.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.onClickChildRow(model);
                }
            });
        }
    }

    public interface FamilyMembersInterface {
//        void setConstraint(ChildModel model, int parentId, int siblingId, int index);

        void onClickChildRow(ChildModel model);
    }
}