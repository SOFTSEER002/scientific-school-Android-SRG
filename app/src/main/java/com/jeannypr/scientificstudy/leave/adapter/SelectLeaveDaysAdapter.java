package com.jeannypr.scientificstudy.leave.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.OnSwipeTouchListner;
import com.jeannypr.scientificstudy.leave.model.RequestedLeaveDaysModel;

import java.util.List;

import static com.jeannypr.scientificstudy.Utilities.TrackLocationTask.TAG;

public class SelectLeaveDaysAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<RequestedLeaveDaysModel> leaveDays;
    public double totalDays;
    OnItemClickListner listner;

    public SelectLeaveDaysAdapter(Context context, List<RequestedLeaveDaysModel> availableLeaves, OnItemClickListner listner) {
        super();
        this.listner = listner;
        mContext = context;
        leaveDays = availableLeaves;
        totalDays = leaveDays.get(0).TotalRequestedDays;
    }

    @Override
    public int getItemCount() {
        return leaveDays.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_select_leave_days, parent, false);
        return new SelectLeaveDaysAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RequestedLeaveDaysModel route = leaveDays.get(position);
        ((MyViewHolder) holder).bind(route, listner);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTabTxt, sliderLbl, holidayTitleTxt;
        FloatingActionButton sliderBtn;
        ConstraintLayout slider, leaveDayRow;

        private boolean isFullday = true;

        MyViewHolder(View itemView) {
            super(itemView);

            leaveDayRow = itemView.findViewById(R.id.leaveDayRow);
            dateTabTxt = itemView.findViewById(R.id.dateTab);
            sliderLbl = itemView.findViewById(R.id.sliderLbl);
            sliderBtn = itemView.findViewById(R.id.sliderBtn);
            slider = itemView.findViewById(R.id.slider);
            holidayTitleTxt = itemView.findViewById(R.id.holidayTitle);
        }

        void bind(final RequestedLeaveDaysModel leaveDay, SelectLeaveDaysAdapter.OnItemClickListner listner) {
            dateTabTxt.setText(leaveDay.FormattedDate);

            if (leaveDay.IsHoliday) {
                holidayTitleTxt.setVisibility(View.VISIBLE);
                slider.setVisibility(View.GONE);

                holidayTitleTxt.setText(leaveDay.HolidayTitle);
                leaveDayRow.setBackgroundColor(mContext.getResources().getColor(R.color.red5));
                dateTabTxt.setTextColor(mContext.getResources().getColor(R.color.red3));


            } else {
                holidayTitleTxt.setVisibility(View.GONE);
                slider.setVisibility(View.VISIBLE);
                leaveDayRow.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                dateTabTxt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                switch (leaveDay.DayType) {
                    case Constants.LeaveDayType.FULLDAY:
                        sliderLbl.setText("F");
                        isFullday = false;
                        MoveLeftOrRight(leaveDay, false);
                        break;

                    case Constants.LeaveDayType.HALFDAY:
                        sliderLbl.setText("H");
                        isFullday = true;
                        MoveLeftOrRight(leaveDay, false);
                        break;
                }


                SetSliderTouchEvent(leaveDay, listner);
            }

        }

        private void SetSliderTouchEvent(final RequestedLeaveDaysModel leaveDay, final OnItemClickListner listner) {
            slider.setOnTouchListener(new OnSwipeTouchListner(mContext) {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return super.onTouch(view, motionEvent);
                }

                @Override
                public void onClick() {
                    super.onClick();
                    Log.i(TAG, "Slider Clicked");

                    boolean isClicked = true;
                    MoveLeftOrRight(leaveDay, isClicked);
                    /*if (!isFullday) {
                        totalDays += 0.5;
                    } else {
                        totalDays -= 0.5;
                    }*/

                    listner.OnStatusChange(totalDays);
                }
            });
        }

        private void MoveLeftOrRight(RequestedLeaveDaysModel leaveDay, boolean isClicked) {
            if (!isFullday) {
                //left swipe
                if (isClicked) {
                    totalDays += 0.5;
                }

                leaveDays.get(0).TotalRequestedDays = totalDays;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    slider.setBackground(mContext.getResources().getDrawable(R.drawable.blue_bg_round_corner));
                    sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.white)));
                    // swipeBtn.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    // swipeBtn.setImageResource(R.drawable.ic_right_arrow);
                    sliderLbl.setTextColor(mContext.getResources().getColor(R.color.white));
                }

                ConstraintSet set2 = new ConstraintSet();
                set2.clone(slider);
                set2.connect(sliderLbl.getId(), ConstraintSet.END, slider.getId(), ConstraintSet.END);
                set2.connect(sliderLbl.getId(), ConstraintSet.START, sliderBtn.getId(), ConstraintSet.END);
                set2.applyTo(slider);

                ConstraintSet set = new ConstraintSet();
                set.clone(slider);
                set.connect(sliderBtn.getId(), ConstraintSet.START, slider.getId(), ConstraintSet.START, 4);
                set.clear(sliderBtn.getId(), ConstraintSet.END);
                set.applyTo(slider);

                sliderLbl.setText("F");
                isFullday = true;
                leaveDay.DayType = Constants.LeaveDayType.FULLDAY;

            } else {
                //right swipe

                if (isClicked) {
                    totalDays -= 0.5;
                }
                leaveDays.get(0).TotalRequestedDays = totalDays;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    slider.setBackground(mContext.getResources().getDrawable(R.drawable.white_bg_round_corner));
                    sliderBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimary)));
                    // swipeBtn.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                    //      swipeBtn.setImageResource(R.drawable.left_arrow_white);
                    sliderLbl.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                }

                ConstraintSet set2 = new ConstraintSet();
                set2.clone(slider);
                set2.connect(sliderLbl.getId(), ConstraintSet.START, slider.getId(), ConstraintSet.START);
                set2.connect(sliderLbl.getId(), ConstraintSet.END, sliderBtn.getId(), ConstraintSet.START);
                set2.applyTo(slider);

                ConstraintSet set = new ConstraintSet();
                set.clone(slider);
                set.connect(sliderBtn.getId(), ConstraintSet.END, slider.getId(), ConstraintSet.END, 4);
                set.clear(sliderBtn.getId(), ConstraintSet.START);
                set.applyTo(slider);

                sliderLbl.setText("H");
                isFullday = false;
                leaveDay.DayType = Constants.LeaveDayType.HALFDAY;
            }
        }
    }

    public interface OnItemClickListner {
        void OnStatusChange(double totalDays);
    }
}


