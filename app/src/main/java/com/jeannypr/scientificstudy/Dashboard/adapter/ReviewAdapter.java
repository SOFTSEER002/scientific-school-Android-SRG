package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Dashboard.model.ReviewModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowReviewAuditSectionBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    Context mContext;
    private List<ReviewModel> list;
//    private HolidaySectionNavigator mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReviewAdapter(Context context, ArrayList<ReviewModel> data) {
        super();
        mContext = context;
        list = data;
//        this.mNavigator = listner;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowReviewAuditSectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_review_audit_section, parent, false);
        return new ReviewAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ReviewModel itemDetail = list.get(position);
        holder.bind(itemDetail);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowReviewAuditSectionBinding itemBinding;

        MyViewHolder(RowReviewAuditSectionBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ReviewModel model) {
            String reviewDate = "";
            try {
                reviewDate = Utility.GetFormattedDateDMY(model.getValue(), Constants.DATE_FORMAT_MDY,Constants.DATE_FORMAT_DMY);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (reviewDate != null && !reviewDate.equals(""))
                model.setValue(reviewDate);
            itemBinding.setViewModel(model);


        }
    }

//    public interface HolidaySectionNavigator {
//        void onClickRemingMe(int eventId, String eventEndDate);
//
//        void showFullDesc(String description);
//    }
}