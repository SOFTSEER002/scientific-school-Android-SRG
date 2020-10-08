package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowHolidayChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    private HolidaySectionNavigator mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public HolidayAdapter(Context context, ArrayList<HomeTabItemDetail> data, HolidaySectionNavigator listner) {
        super();
        mContext = context;
        list = data;
        this.mNavigator = listner;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowHolidayChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_holiday_child_home_tab, parent, false);
        return new HolidayAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail itemDetail = list.get(position);
        itemDetail.adapterPosition = position;
        holder.bind(itemDetail);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowHolidayChildHomeTabBinding itemBinding;

        MyViewHolder(RowHolidayChildHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabItemDetail model) {
            itemBinding.setViewModel(model);
            String reminderDate;

//            if (model.getExtraKeys().getReminderSet()) {
//                Date dateObj = Utility.StringDateToDate(model.getExtraKeys().getReminderDate());
//                reminderDate = Utility.GetFormattedDateDMY(dateObj);
//
//            } else reminderDate = "";
//
//            itemBinding.reminderDate.setText(reminderDate);

            itemBinding.reminderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.onClickRemingMe(model.getID(), model.getEndDate());
                }
            });

            itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.showFullDesc(model.getDescription(),model.getSubtitle());
                }
            });

        }
    }

    public interface HolidaySectionNavigator {
        void onClickRemingMe(int eventId, String eventEndDate);

        void showFullDesc(String description, String title);
    }
}