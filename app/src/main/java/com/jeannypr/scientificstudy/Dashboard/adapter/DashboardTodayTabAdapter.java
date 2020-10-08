package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Classwork.activity.CwHwListActivity;
import com.jeannypr.scientificstudy.Dashboard.fragment.BroadcastMsgFragment;
import com.jeannypr.scientificstudy.Dashboard.fragment.FeeReminderFragment;
import com.jeannypr.scientificstudy.Dashboard.fragment.SMSFragment;
import com.jeannypr.scientificstudy.Dashboard.model.AuditAnalyticsModel;
import com.jeannypr.scientificstudy.Dashboard.model.FeeSummary;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.model.RegSummaryModel;
import com.jeannypr.scientificstudy.Dashboard.model.ReviewModel;
import com.jeannypr.scientificstudy.Dashboard.model.TodayTabExtraKeysModel;
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardTodayTabNavigator;
import com.jeannypr.scientificstudy.Exam.activity.EnterMarkSelectClassActivity;
import com.jeannypr.scientificstudy.Fee.activity.FeeModuleActivity;
import com.jeannypr.scientificstudy.Inventory.activity.InventoryModuleActivity;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.activity.RegistrationModuleActivity;
import com.jeannypr.scientificstudy.Timetable.activity.StaffTimetableModuleActivity;
import com.jeannypr.scientificstudy.Timetable.activity.StudentTimetableModuleActivity;
import com.jeannypr.scientificstudy.Timetable.activity.TimetableModuleActivity;
import com.jeannypr.scientificstudy.Transport.activity.TransportModuleActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.RowAcademicTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowAuditTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowChatTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowFeeTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowHealthTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowLearnTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowMessengerTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowRegTodayTabBinding;
import com.jeannypr.scientificstudy.databinding.RowTimetableTodayTabBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DashboardTodayTabAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<HomeTabDataListModel> feedItems;
    private static final int VIEW_TYPE_FEE = 1;
    private static final int VIEW_TYPE_REG = 2;
    private static final int VIEW_TYPE_MESSENGER = 3;

    private static final int VIEW_TYPE_CHAT = 4;
    private static final int VIEW_TYPE_STAFF = 5;
    private static final int VIEW_TYPE_TIMETABLE = 6;
    private static final int VIEW_TYPE_ACADEMIC = 7;

    private static final int VIEW_TYPE_LEARN = 8;
    private static final int VIEW_TYPE_HEALTH = 9;
    private static final int VIEW_TYPE_AUDIT = 10;
    private DashboardTodayTabNavigator mNavigator;
    FragmentManager fragmentManager;

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public DashboardTodayTabAdapter(Context context, ArrayList<HomeTabDataListModel> items, DashboardTodayTabNavigator listner, FragmentManager childFragmentManager) {
        super();
        mContext = context;
        feedItems = items;
        this.mNavigator = listner;
        this.fragmentManager = childFragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        HomeTabDataListModel model = feedItems.get(position);

        switch (model.type.trim()) {
            case Constants.TodayTabSections.FEE:
                return VIEW_TYPE_FEE;
            case Constants.TodayTabSections.REG:
                return VIEW_TYPE_REG;
            case Constants.TodayTabSections.MESSENGER:
                return VIEW_TYPE_MESSENGER;

            case Constants.TodayTabSections.CHAT:
                return VIEW_TYPE_CHAT;
//            case Constants.TodayTabSections.STAFF:
//                return VIEW_TYPE_STAFF;
            case Constants.TodayTabSections.TIMETABLE:
                return VIEW_TYPE_TIMETABLE;
            case Constants.TodayTabSections.ACADEMIC:
                return VIEW_TYPE_ACADEMIC;


            case Constants.TodayTabSections.LEARN:
                return VIEW_TYPE_LEARN;

            case Constants.TodayTabSections.AUDIT:
                return VIEW_TYPE_AUDIT;

            default: //Constants.TodayTabSections.HEALTH:
                return VIEW_TYPE_HEALTH;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FEE) {
            return new FeeViewHolder((RowFeeTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_fee_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_REG) {
            return new RegViewHolder((RowRegTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_reg_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_MESSENGER) {
            return new MessengerViewHolder((RowMessengerTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_messenger_today_tab, parent, false));
        } else if (viewType == VIEW_TYPE_TIMETABLE) {
            return new TimetableViewHolder((RowTimetableTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_timetable_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_ACADEMIC) {
            return new AcademicViewHolder((RowAcademicTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_academic_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_CHAT) {
            return new ChatViewHolder((RowChatTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_chat_today_tab, parent, false));
        } else if (viewType == VIEW_TYPE_LEARN) {
            return new LearnViewHolder((RowLearnTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_learn_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_HEALTH) {
            return new HealthViewHolder((RowHealthTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_health_today_tab, parent, false));

        } else if (viewType == VIEW_TYPE_AUDIT) {
            return new AuditViewHolder((RowAuditTodayTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_audit_today_tab, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HomeTabDataListModel model = feedItems.get(position);
        model.adapterPosition = position;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_FEE:
                ((FeeViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_REG:
                ((RegViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_TIMETABLE:
                ((TimetableViewHolder) holder).bind(model);
                break;

            case VIEW_TYPE_ACADEMIC:
                ((AcademicViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_MESSENGER:
                ((MessengerViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_CHAT:
                ((ChatViewHolder) holder).bind(model);
                break;

            case VIEW_TYPE_LEARN:
                ((LearnViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_HEALTH:
                ((HealthViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_AUDIT:
                ((AuditViewHolder) holder).bind(model);
                break;
        }
    }

    class FeeViewHolder extends RecyclerView.ViewHolder {

        public RowFeeTodayTabBinding itemBinding;

        FeeViewHolder(RowFeeTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.setViewModel(model.getDetails());
                itemBinding.feeSection.setVisibility(View.VISIBLE);
                itemBinding.feeSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                setFeeButtonListner(model.getDetails().getExtraKeys().getFeesEnabled());
//                if (!model.getDetails().getExtraKeys().getFeesEnabled())
//                    itemBinding.feeChart.setNoDataText(mContext.getResources().getString(R.string.moduleNotPermitted));
                setChartData(model.getDetails().getExtraKeys().getFeeSummary(), model.getDetails().getExtraKeys().getFeesEnabled());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });
            } else {
                itemBinding.feeSection.setVisibility(View.GONE);
                itemBinding.feeSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setFeeButtonListner(final Boolean isFeesEnabled) {
            itemBinding.btnGoToFee.setOnClickListener(new View.OnClickListener() {
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
            });
        }

        private void setChartData(ArrayList<FeeSummary> regAdmSummary, Boolean isFeesEnabled) {

            if (!isFeesEnabled) {
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
            }

        }

        private void createBarGraph(BarDataSet barDataSet, IndexAxisValueFormatter formatter) {
            barDataSet.setBarBorderWidth(Constants.BarGraphSettings.BAR_BORDER_WIDTH);
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
        }
    }

    class RegViewHolder extends RecyclerView.ViewHolder {

        public RowRegTodayTabBinding itemBinding;

        RegViewHolder(RowRegTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.regSection.setVisibility(View.VISIBLE);
                itemBinding.regSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                setRegButtonListner(model.getDetails().getExtraKeys().getRegistratioEnabled());

//                if (!model.getDetails().getExtraKeys().getFeesEnabled())
//                    itemBinding.regChart.setNoDataText(mContext.getResources().getString(R.string.moduleNotPermitted));
                setChartData(model.getDetails().getExtraKeys().getRegAdmSummary(), model.getDetails().getExtraKeys().getRegistratioEnabled());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });
            } else {
                itemBinding.regSection.setVisibility(View.GONE);
                itemBinding.regSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setChartData(ArrayList<RegSummaryModel> regAdmSummary, Boolean isRegistratioEnabled) {
            if (!isRegistratioEnabled) {
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
            }
        }

        private void createBarGraph(BarDataSet barDataSet, IndexAxisValueFormatter formatter) {
            barDataSet.setBarBorderWidth(Constants.BarGraphSettings.BAR_BORDER_WIDTH); //0.9f
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
            itemBinding.regChart.invalidate();
        }

        private void setRegButtonListner(final Boolean isRegistratioEnabled) {
            itemBinding.btnGoToReg.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    class TimetableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowTimetableTodayTabBinding itemBinding;
        Boolean isMyTimetableEnabled, isClassTTEnabled, isStaffTTEnabled;

        TimetableViewHolder(RowTimetableTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.timetableSection.setVisibility(View.VISIBLE);
                itemBinding.timetableSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                setButtonsListner(model.getDetails().getExtraKeys().getMyTimetableEnabled(), model.getDetails().getExtraKeys().getStudentTimetableEnabled(),
                        model.getDetails().getExtraKeys().getStaffTimetableEnabled());
            } else {
                itemBinding.timetableSection.setVisibility(View.GONE);
                itemBinding.timetableSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setButtonsListner(Boolean myTimetableEnabled, Boolean studentTimetableEnabled, Boolean staffTimetableEnabled) {
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
    }

    class AcademicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowAcademicTodayTabBinding itemBinding;
        Boolean isHWEnabled, isCWEnabled, isExamEnabled, isTransportEnabled, isInventoryEnabled, isAccountsEnabled;

        AcademicViewHolder(RowAcademicTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.academicSection.setVisibility(View.VISIBLE);
                itemBinding.academicSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());

                setButtonsListner(model.getDetails().getExtraKeys());

            } else {
                itemBinding.academicSection.setVisibility(View.GONE);
                itemBinding.academicSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setButtonsListner(TodayTabExtraKeysModel extraKeys) {
            isHWEnabled = extraKeys.getHWEnabled();
            isCWEnabled = extraKeys.getCWEnabled();
            isExamEnabled = extraKeys.getExamEnabled();
            isInventoryEnabled = extraKeys.getInventoryEnabled();
            isTransportEnabled = extraKeys.getTransportEnabled();
            isAccountsEnabled = extraKeys.getAccountsEnabled();

            itemBinding.hwBtn.setOnClickListener(this);
            itemBinding.cwBtn.setOnClickListener(this);
            itemBinding.examBtn.setOnClickListener(this);
            itemBinding.transportBtn.setOnClickListener(this);
            itemBinding.inventoryBtn.setOnClickListener(this);
            itemBinding.accountsBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;

            switch (view.getId()) {
                case R.id.hwBtn:

                    if (isHWEnabled) {
                        intent = new Intent(mContext, CwHwListActivity.class);
                        intent.putExtra(Constants.ACTIVITY_TYPE, Constants.DiaryType.Homework);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;

                case R.id.cwBtn:
                    if (isCWEnabled) {
                        intent = new Intent(mContext, CwHwListActivity.class);
                        intent.putExtra(Constants.ACTIVITY_TYPE, Constants.DiaryType.Classwork);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;

                case R.id.inventoryBtn:
                    if (isInventoryEnabled) {
                        intent = new Intent(mContext, InventoryModuleActivity.class);
                        intent.putExtra(Constants.MODULE_TYPE, Constants.Module.INVENTORY);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    break;

                case R.id.examBtn:
                    if (isExamEnabled) {
                        intent = new Intent(mContext, EnterMarkSelectClassActivity.class);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;

                case R.id.transportBtn:
                    if (isTransportEnabled)
                        intent = new Intent(mContext, TransportModuleActivity.class);
                    else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));

                    break;

                case R.id.accountsBtn:
                    if (isAccountsEnabled) {
                        intent = new Intent(mContext, InventoryModuleActivity.class);
                        intent.putExtra(Constants.MODULE_TYPE, Constants.Module.ACCOUNTS);
                    } else
                        mNavigator.showFullDesc(mContext.getResources().getString(R.string.moduleNotPermitted), mContext.getResources().getString(R.string.permission));
                    break;
            }

            if (intent != null)
                mContext.startActivity(intent);
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        public RowChatTodayTabBinding itemBinding;

        ChatViewHolder(RowChatTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.chatSection.setVisibility(View.VISIBLE);
                itemBinding.chatSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                redirectToStaffForChat();
                redirectToGroupForChat();
                redirectToPrivateInboxForChat();

            } else {
                itemBinding.chatSection.setVisibility(View.GONE);
                itemBinding.chatSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void redirectToPrivateInboxForChat() {
            itemBinding.inboxChatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatGroupActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }

        private void redirectToGroupForChat() {
            itemBinding.classChatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatGroupActivity.class);
                    intent.putExtra(Constants.SELECTED_TAB, Constants.ChatGroupTab.CLASS);
                    mContext.startActivity(intent);
                }
            });
        }

        private void redirectToStaffForChat() {
            itemBinding.staffChatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatGroupActivity.class);
                    intent.putExtra(Constants.SELECTED_TAB, Constants.ChatGroupTab.STAFF);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class MessengerViewHolder extends RecyclerView.ViewHolder {

        public RowMessengerTodayTabBinding itemBinding;

        MessengerViewHolder(RowMessengerTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            itemBinding.setViewModel(model.getDetails());
            itemBinding.pb.setVisibility(View.VISIBLE);
            setupViewPager(itemBinding.viewpager);
            itemBinding.messengerTabs.setupWithViewPager(itemBinding.viewpager);
            itemBinding.messengerSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        private void setupViewPager(ViewPager viewPager) {
//            ViewPagerAdapter adapter = new ViewPagerAdapter(((AppCompatActivity) mContext).getSupportFragmentManager());
            ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
            adapter.addFragment(BroadcastMsgFragment.newInstance(), mContext.getString(R.string.broadcast));
            adapter.addFragment(new SMSFragment(), mContext.getString(R.string.sms_tab));
            adapter.addFragment(new FeeReminderFragment(), mContext.getString(R.string.fee_reminder_tab));
            viewPager.setAdapter(adapter);
            itemBinding.pb.setVisibility(View.GONE);
        }

        class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFragment(Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }

        }
    }

    class LearnViewHolder extends RecyclerView.ViewHolder {

        public RowLearnTodayTabBinding itemBinding;

        LearnViewHolder(RowLearnTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.learningSection.setVisibility(View.VISIBLE);
                itemBinding.learningSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });
                itemBinding.requestBtnLearnTab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebView(model.getDetails().getExtraKeys().getButtonOnClickLink(), model.getDetails().getTitle());
                    }
                });
            } else {
                itemBinding.learningSection.setVisibility(View.GONE);
                itemBinding.learningSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void openWebView(String link, String title) {
            Intent signupIntent = new Intent(mContext, HelpActivity.class);
            signupIntent.putExtra(Constants.WEB_URL, link);
            signupIntent.putExtra(Constants.TITLE, title);
            signupIntent.putExtra(Constants.SUBTITLE, "");
            mContext.startActivity(signupIntent);
        }
    }

    class HealthViewHolder extends RecyclerView.ViewHolder {

        public RowHealthTodayTabBinding itemBinding;

        HealthViewHolder(RowHealthTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.healthSection.setVisibility(View.VISIBLE);
                itemBinding.healthSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });
                itemBinding.requestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openWebView(model.getDetails().getExtraKeys().getButtonOnClickLink(), model.getDetails().getTitle());
                    }
                });
            } else {
                itemBinding.healthSection.setVisibility(View.GONE);
                itemBinding.healthSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void openWebView(String link, String title) {
            Intent signupIntent = new Intent(mContext, HelpActivity.class);
            signupIntent.putExtra(Constants.WEB_URL, link);
            signupIntent.putExtra(Constants.TITLE, title);
            signupIntent.putExtra(Constants.SUBTITLE, "");
            mContext.startActivity(signupIntent);
        }
    }

    class AuditViewHolder extends RecyclerView.ViewHolder {

        public RowAuditTodayTabBinding itemBinding;

        AuditViewHolder(RowAuditTodayTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getDetails() != null) {
                itemBinding.auditSection.setVisibility(View.VISIBLE);
                itemBinding.auditSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getDetails());

                setContacts(model.getDetails().getExtraKeys().getPrincipalContactNo(), model.getDetails().getExtraKeys().getAdminContactNo());
                setAnalyticsButtonListner(model.getDetails().getExtraKeys().getParentAnalytics(), model.getDetails().getExtraKeys().getAdminAnalytics());
                setParentsAnalytics(model.getDetails().getExtraKeys().getParentAnalytics());
                setReviewData(model.getDetails().getExtraKeys().AdvancedReview);

                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getDetails().getDescription(), model.getDetails().getTitle());
                    }
                });

            } else {
                itemBinding.auditSection.setVisibility(View.GONE);
                itemBinding.auditSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setReviewData(ArrayList<ReviewModel> advancedReview) {
            ReviewAdapter adapter = new ReviewAdapter(mContext, advancedReview);
            itemBinding.reviewRV.setAdapter(adapter);

            itemBinding.arrowIcReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemBinding.reviewRV.getVisibility() == View.VISIBLE)
                        itemBinding.reviewRV.setVisibility(View.GONE);
                    else itemBinding.reviewRV.setVisibility(View.VISIBLE);
                }
            });
        }

        private void setAnalyticsButtonListner(final AuditAnalyticsModel parentAnalytics, final AuditAnalyticsModel staffAnalytics) {
            itemBinding.parentsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setParentsAnalytics(parentAnalytics);
                }
            });

            itemBinding.staffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemBinding.positiveAnalysis.setText(staffAnalytics.getUsingApp() + " " + mContext.getString(R.string.staffsUsingApp));
                    itemBinding.negativeAnalysis.setText(staffAnalytics.getNotUsingApp() + " " + mContext.getString(R.string.staffsNotUsingApp));
                    setPieChart(staffAnalytics.getNotUsingApp(), (staffAnalytics.getNotUsingApp() + staffAnalytics.getUsingApp()));
                    itemBinding.auditSection.setBackgroundColor(mContext.getResources().getColor(R.color.pink3));
                    itemBinding.tvAuditTitle.setTextColor(mContext.getResources().getColor(R.color.white));

                    String fromDate = "";
                    String toDate = "";
                    try {
                        fromDate = Utility.GetFormattedDateDM(staffAnalytics.getAnalyticsFromDate());
                        toDate = Utility.GetFormattedDateDM(staffAnalytics.getAnalyticsToDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if ((fromDate != null && !fromDate.equals("")) && (toDate != null && !toDate.equals("")))
                        itemBinding.negativeAnalysisDesc.setText(mContext.getString(R.string.auditDesc) + " " + fromDate + " to " + toDate);
                }
            });
        }

        private void setParentsAnalytics(AuditAnalyticsModel parentAnalytics) {
            itemBinding.positiveAnalysis.setText(parentAnalytics.getUsingApp() + " " + mContext.getString(R.string.parentsUsingApp));
            itemBinding.negativeAnalysis.setText(parentAnalytics.getNotUsingApp() + " " + mContext.getString(R.string.parentsNotUsingApp));
            setPieChart(parentAnalytics.getNotUsingApp(), (parentAnalytics.getNotUsingApp() + parentAnalytics.getUsingApp()));
            itemBinding.auditSection.setBackgroundColor(mContext.getResources().getColor(R.color.pink7));
            itemBinding.tvAuditTitle.setTextColor(mContext.getResources().getColor(R.color.orange8));

            String fromDate = "";
            String toDate = "";
            try {
                fromDate = Utility.GetFormattedDateDM(parentAnalytics.getAnalyticsFromDate());
                toDate = Utility.GetFormattedDateDM(parentAnalytics.getAnalyticsToDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((fromDate != null && !fromDate.equals("")) && (toDate != null && !toDate.equals("")))
                itemBinding.negativeAnalysisDesc.setText(mContext.getString(R.string.auditDesc) + " " + fromDate + " to " + toDate);
        }

        private void setPieChart(int notUsingApp, int totalUsers) {
            if (totalUsers != 0) {
                int percent = (notUsingApp * 100) / totalUsers;

                if (percent == 0) itemBinding.auditChart.setVisibility(View.GONE);
                else {
                    itemBinding.auditChart.setVisibility(View.VISIBLE);
                    itemBinding.foreProgressbar.setProgress(percent);
                }
            } else itemBinding.auditChart.setVisibility(View.GONE);


        }

        private void setContacts(final String principalContactNo, final String adminContactNo) {
            itemBinding.principalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeCall(principalContactNo);
                }
            });

            itemBinding.adminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeCall(adminContactNo);
                }
            });
        }

        private void makeCall(String contactNo) {
            Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
            dialPadIntent.setData(Uri.parse("tel:" + contactNo));
            mContext.startActivity(dialPadIntent);
        }
    }
}