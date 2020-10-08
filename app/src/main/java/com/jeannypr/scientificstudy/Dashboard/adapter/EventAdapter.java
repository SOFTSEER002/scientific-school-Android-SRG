package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.Dashboard.navigator.EventNavigator;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowBdayChildHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowEventChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    private EventNavigator mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public EventAdapter(Context context, ArrayList<HomeTabItemDetail> data, EventNavigator listner) {
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

        RowEventChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_event_child_home_tab, parent, false);
        return new EventAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail itemDetail = list.get(position);
        itemDetail.adapterPosition = position;
        holder.bind(itemDetail);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowEventChildHomeTabBinding itemBinding;

        MyViewHolder(RowEventChildHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabItemDetail model) {
            itemBinding.setViewModel(model);

//            if (model.getExtraKeys().getCheckedIn()) {
//                itemBinding.checkInIc.setText(mContext.getString(R.string.checkedIn));
//                itemBinding.goingLbl.setEnabled(false);
//            } else {
                setCheckInListner(model.getID(), model.adapterPosition);
//                itemBinding.checkInIc.setText(mContext.getString(R.string.checkIn));
//                itemBinding.goingLbl.setEnabled(true);
//            }

            rsvp(model.getID(), model.getExtraKeys().getRsvp());

            itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.showFullDesc(model.getDescription(), model.getSubtitle());
                }
            });
        }

        private void setCheckInListner(final int id, final int adapterPosition) {
            itemBinding.checkInIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.checkIn(id, adapterPosition);
                }
            });
        }

        private void rsvp(final int eventId, int rsvp) {
            itemBinding.goingLbl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String rsvp = adapterView.getItemAtPosition(i).toString();
                    if (!rsvp.equals("") && !rsvp.equals(mContext.getResources().getString(R.string.selectRSVP)))
                        mNavigator.rsvp(eventId, rsvp);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            itemBinding.goingLbl.setSelection(rsvp);
        }

        private int getIndex(int rsvpStatusId) {
            int index = 0;

            switch (rsvpStatusId) {
                case 1:
                    index = 2;
                    break;

                case 2:
                    index = 1;
                    break;

                case 3:
                    index = 3;
                    break;
            }

            return index;
        }
    }

    public interface EventInterface {
        void showFullDesc(String description);
    }
}