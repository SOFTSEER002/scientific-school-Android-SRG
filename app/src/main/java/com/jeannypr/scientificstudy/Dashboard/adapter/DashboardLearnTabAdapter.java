package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Dashboard.model.FeeSummary;
import com.jeannypr.scientificstudy.Dashboard.model.LearnSubjectList;
import com.jeannypr.scientificstudy.Dashboard.model.LearnTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.model.RegSummaryModel;
import com.jeannypr.scientificstudy.Dashboard.navigator.LearnTabNavigator;
import com.jeannypr.scientificstudy.LearnSubject.activity.SubjectDetailsActivity;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowLearnCyberTabBinding;
import com.jeannypr.scientificstudy.databinding.RowLearnShareTabBinding;
import com.jeannypr.scientificstudy.databinding.RowLearnSubjectTabBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardLearnTabAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<LearnTabDataListModel> feedItems;
    private static final int VIEW_TYPE_SUBJECT = 1;
    private static final int VIEW_TYPE_SHARE = 2;
    private static final int VIEW_TYPE_CYBER = 3;
    private LearnTabNavigator mNavigator;

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public DashboardLearnTabAdapter(Context context, ArrayList<LearnTabDataListModel> items, LearnTabNavigator listner) {
        super();
        mContext = context;
        feedItems = items;
        this.mNavigator = listner;
    }

    @Override
    public int getItemViewType(int position) {
        LearnTabDataListModel model = feedItems.get(position);

        switch (model.type.trim()) {
            case Constants.LearnTabSections.SUBJECT:
                return VIEW_TYPE_SUBJECT;
            case Constants.LearnTabSections.SHARE:
                return VIEW_TYPE_SHARE;

            default: //Constants.TodayTabSections.HEALTH:
                return VIEW_TYPE_CYBER;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SUBJECT) {
            return new SubjectViewHolder((RowLearnSubjectTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_learn_subject_tab, parent, false));

        } else if (viewType == VIEW_TYPE_SHARE) {
            return new ShareViewHolder((RowLearnShareTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_learn_share_tab, parent, false));
        } else if (viewType == VIEW_TYPE_CYBER) {
            return new CybertableViewHolder((RowLearnCyberTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_learn_cyber_tab, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LearnTabDataListModel model = feedItems.get(position);
        model.adapterPosition = position;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SUBJECT:
                ((SubjectViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_SHARE:
                ((ShareViewHolder) holder).bind(model);
                break;

            case VIEW_TYPE_CYBER:
                ((CybertableViewHolder) holder).bind(model);
                break;
        }
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {

        public RowLearnSubjectTabBinding itemBinding;

        SubjectViewHolder(RowLearnSubjectTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final LearnTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.setViewModel(model.getDetails());
                itemBinding.subjectSection.setVisibility(View.VISIBLE);
                itemBinding.subjectSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                final ArrayList<LearnSubjectList> learnSubjectLists = new ArrayList<>(model.getDetails().getExtraKeys().getSubjects());
                LearnDashboardToolSubjectAdapter toolSubjectAdapter = new LearnDashboardToolSubjectAdapter(mContext,learnSubjectLists);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2, GridLayoutManager.HORIZONTAL, false);
                itemBinding.rvSubject.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                itemBinding.rvSubject.setAdapter(toolSubjectAdapter);
                toolSubjectAdapter.setOnItemClickListener(new LearnDashboardToolSubjectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent subjectModuleIntent = new Intent(mContext, SubjectDetailsActivity.class);
                        subjectModuleIntent.putExtra("sunject_name",learnSubjectLists.get(position).subjectName );
                        subjectModuleIntent.putExtra("sunject_id",learnSubjectLists.get(position).subjectId );
                        mContext.startActivity(subjectModuleIntent);
                    }
                });



               /* setFeeButtonListner(model.getDetails().getExtraKeys().getFeesEnabled());
                setChartData(model.getDetails().getExtraKeys().getFeeSummary(), model.getDetails().getExtraKeys().getFeesEnabled());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });*/
            } else {
                itemBinding.subjectSection.setVisibility(View.GONE);
                itemBinding.subjectSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setFeeButtonListner(final Boolean isFeesEnabled) {
            /*itemBinding.btnGoToFee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isFeesEnabled) {
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    } else {
                        Intent intent = new Intent(mContext, FeeModuleActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });

            itemBinding.btnCloseChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFeesEnabled) {
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    } else {
                        if (itemBinding.feeChart.getVisibility() == View.VISIBLE) {
                            itemBinding.feeChart.setVisibility(View.GONE);

                            Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
                            downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                            itemBinding.btnCloseChart.setCompoundDrawables(downArrowIc, null, null, null);
                            itemBinding.btnCloseChart.setText(mContext.getResources().getString(R.string.viewChart));

                        } else {
                            itemBinding.feeChart.setVisibility(View.VISIBLE);

                            Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_up);
                            downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                            itemBinding.btnCloseChart.setCompoundDrawables(downArrowIc, null, null, null);
                            itemBinding.btnCloseChart.setText(mContext.getResources().getString(R.string.closeChart));
                        }
                    }

                }
            });*/
        }

        private void setChartData(ArrayList<FeeSummary> regAdmSummary, Boolean isFeesEnabled) {

           /* if (!isFeesEnabled) {
                itemBinding.feeChart.setVisibility(View.GONE);

                //if not permitted, then reset UI of button.
                Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
                downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                itemBinding.btnCloseChart.setCompoundDrawables(downArrowIc, null, null, null);
                itemBinding.btnCloseChart.setText(mContext.getResources().getString(R.string.viewChart));

            } else {
                itemBinding.feeChart.setVisibility(View.VISIBLE);
                ArrayList<BarEntry> NoOfReg = new ArrayList();
                ArrayList classes = new ArrayList<String>();

                if (regAdmSummary != null && regAdmSummary.size() > 0) {
                    for (FeeSummary summary : regAdmSummary) {
                        NoOfReg.add(new BarEntry(regAdmSummary.indexOf(summary), (summary.AmountPaid)));

                        classes.add(summary.Month);
                    }
                }

                BarDataSet barDataSet = new BarDataSet(NoOfReg, mContext.getResources().getString(R.string.collection));
                IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(classes);
                createBarGraph(barDataSet, formatter);
            }*/

        }

        private void createBarGraph(BarDataSet barDataSet, IndexAxisValueFormatter formatter) {
           /* barDataSet.setBarBorderWidth(Constants.BarGraphSettings.BAR_BORDER_WIDTH);
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(barDataSet);

            XAxis xAxis = itemBinding.feeChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(Constants.BarGraphSettings.GRANULARITY);
            xAxis.setValueFormatter(formatter);

            YAxis yAxisRight = itemBinding.feeChart.getAxisRight();
            yAxisRight.setEnabled(false);

            itemBinding.feeChart.setData(barData);
            itemBinding.feeChart.setVisibleXRangeMaximum(Constants.BarGraphSettings.VISIBLE_X_RANGE_MAX);
            itemBinding.feeChart.moveViewToX(Constants.BarGraphSettings.VIEW_TO_X);

            itemBinding.feeChart.setFitBars(true);
            itemBinding.feeChart.animateXY(Constants.BarGraphSettings.DURATION_MILLIS_X, Constants.BarGraphSettings.DURATION_MILLIS_X);
            itemBinding.feeChart.invalidate();
        }*/
        }
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        public RowLearnShareTabBinding itemBinding;

        ShareViewHolder(RowLearnShareTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final LearnTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.shareSection.setVisibility(View.VISIBLE);
                itemBinding.shareSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
               /* setRegButtonListner(model.getDetails().getExtraKeys().getRegistratioEnabled());

                setChartData(model.getDetails().getExtraKeys().getRegAdmSummary(), model.getDetails().getExtraKeys().getRegistratioEnabled());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });*/
            } else {
                //itemBinding.regSection.setVisibility(View.GONE);
                //itemBinding.regSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setChartData(ArrayList<RegSummaryModel> regAdmSummary, Boolean isRegistratioEnabled) {
            /*if (!isRegistratioEnabled) {
                itemBinding.regChart.setVisibility(View.GONE);

                //if not permitted, then reset UI of button.
                Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
                downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                itemBinding.btnCloseChartReg.setCompoundDrawables(downArrowIc, null, null, null);
                itemBinding.btnCloseChartReg.setText(mContext.getResources().getString(R.string.viewChart));

            } else {
                ArrayList<BarEntry> NoOfReg = new ArrayList();
                ArrayList classes = new ArrayList<String>();

                if (regAdmSummary != null && regAdmSummary.size() > 0) {
                    for (RegSummaryModel regSummaryModel : regAdmSummary) {
                        NoOfReg.add(new BarEntry(regAdmSummary.indexOf(regSummaryModel), (regSummaryModel.TotalReg)));

                        classes.add(regSummaryModel.ClassName);
                    }
                }

                BarDataSet barDataSet = new BarDataSet(NoOfReg, mContext.getResources().getString(R.string.noOfReg));
                IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(classes);
                createBarGraph(barDataSet, formatter);
            }*/
        }

        private void createBarGraph(BarDataSet barDataSet, IndexAxisValueFormatter formatter) {
           /* barDataSet.setBarBorderWidth(Constants.BarGraphSettings.BAR_BORDER_WIDTH); //0.9f
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(barDataSet);

            XAxis xAxis = itemBinding.regChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(Constants.BarGraphSettings.GRANULARITY);
            xAxis.setValueFormatter(formatter);

            YAxis yAxisRight = itemBinding.regChart.getAxisRight();
            yAxisRight.setEnabled(false);

            itemBinding.regChart.setData(barData);
            itemBinding.regChart.setVisibleXRangeMaximum(Constants.BarGraphSettings.VISIBLE_X_RANGE_MAX);
            itemBinding.regChart.moveViewToX(Constants.BarGraphSettings.VIEW_TO_X);

            itemBinding.regChart.setFitBars(true);
            itemBinding.regChart.animateXY(Constants.BarGraphSettings.DURATION_MILLIS_X, Constants.BarGraphSettings.DURATION_MILLIS_X);
            itemBinding.regChart.invalidate();*/
        }

        private void setRegButtonListner(final Boolean isRegistratioEnabled) {
            /*itemBinding.btnGoToReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRegistratioEnabled) {
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    } else {

                        Intent intent = new Intent(mContext, RegistrationModuleActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });

            itemBinding.btnCloseChartReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRegistratioEnabled) {
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    } else {
                        if (itemBinding.regChart.getVisibility() == View.VISIBLE) {
                            itemBinding.regChart.setVisibility(View.GONE);

                            Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_down);
                            downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                            itemBinding.btnCloseChartReg.setCompoundDrawables(downArrowIc, null, null, null);
                            itemBinding.btnCloseChartReg.setText(mContext.getResources().getString(R.string.viewChart));

                        } else {
                            itemBinding.regChart.setVisibility(View.VISIBLE);

                            Drawable downArrowIc = mContext.getResources().getDrawable(R.drawable.ic_arrow_up);
                            downArrowIc.setBounds(0, 0, downArrowIc.getIntrinsicWidth(), downArrowIc.getIntrinsicHeight());
                            itemBinding.btnCloseChartReg.setCompoundDrawables(downArrowIc, null, null, null);
                            itemBinding.btnCloseChartReg.setText(mContext.getResources().getString(R.string.closeChart));
                        }
                    }

                }
            });
        }*/
        }
    }

    class CybertableViewHolder extends RecyclerView.ViewHolder{

        public RowLearnCyberTabBinding itemBinding;

        // Boolean isMyTimetableEnabled, isClassTTEnabled, isStaffTTEnabled;
        CybertableViewHolder(RowLearnCyberTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final LearnTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.cyberSection.setVisibility(View.VISIBLE);
                itemBinding.cyberSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                /*setButtonsListner(model.getDetails().getExtraKeys().getMyTimetableEnabled(), model.getDetails().getExtraKeys().getStudentTimetableEnabled(),
                        model.getDetails().getExtraKeys().getStaffTimetableEnabled());*/
            } else {
                //itemBinding.timetableSection.setVisibility(View.GONE);
                //itemBinding.timetableSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setButtonsListner(Boolean myTimetableEnabled, Boolean studentTimetableEnabled, Boolean staffTimetableEnabled) {
/*
            isMyTimetableEnabled = myTimetableEnabled;
            isClassTTEnabled = studentTimetableEnabled;
            isStaffTTEnabled = staffTimetableEnabled;

            itemBinding.myTimetableBtn.setOnClickListener(this);
            itemBinding.classTimetableBtn.setOnClickListener(this);
            itemBinding.staffTimetableBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;

            switch (view.getId()) {
                case R.id.myTimetableBtn:
                    if (isMyTimetableEnabled) {
                        intent = new Intent(mContext, TimetableModuleActivity.class);
                        intent.putExtra(Constants.Timetable_INTENT, Constants.TimetableOf.SELF);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    break;

                case R.id.classTimetableBtn:
                    if (isClassTTEnabled) {
                        intent = new Intent(mContext, StudentTimetableModuleActivity.class);
                        intent.putExtra(Constants.Timetable_INTENT, Constants.TimetableOf.STUDENT);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;

                case R.id.staffTimetableBtn:
                    if (isStaffTTEnabled) {
                        intent = new Intent(mContext, StaffTimetableModuleActivity.class);
                        intent.putExtra(Constants.Timetable_INTENT, Constants.TimetableOf.STAFF);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;
            }
            if (intent != null)
                mContext.startActivity(intent);
        }
*/
        }

    }

}
