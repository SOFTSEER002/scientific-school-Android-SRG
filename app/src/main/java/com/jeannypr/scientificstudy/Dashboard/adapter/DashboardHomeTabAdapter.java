package com.jeannypr.scientificstudy.Dashboard.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabDataListModel;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabExtraKeysModel;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardHomeTabNavigator;
import com.jeannypr.scientificstudy.Dashboard.navigator.EventNavigator;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.DotsIndicatorDecoration;
import com.jeannypr.scientificstudy.databinding.RowAnnouncementHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowBdayMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowDefaultMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowEventMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowHolidayMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowNewsMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowPtmHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowSocialLinksHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowSpecialEventMasterHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowSundayHomeTabBinding;
import com.jeannypr.scientificstudy.databinding.RowSurveyMasterHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardHomeTabAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<HomeTabDataListModel> feedItems;
    private DashboardHomeTabNavigator mNavigator;

    private static final int VIEW_TYPE_ANNOUNCEMENT = 1;
    private static final int VIEW_TYPE_BDAY = 2;
    private static final int VIEW_TYPE_PTM = 3;
    private static final int VIEW_TYPE_HOLIDAY = 4;
    private static final int VIEW_TYPE_NEWNOTICE = 5;
    private static final int VIEW_TYPE_EVENTS = 6;
    private static final int VIEW_TYPE_SPECIALEVENT = 7;
    private static final int VIEW_TYPE_SUNDAY = 8;
    private static final int VIEW_TYPE_SURVEY = 9;
    private static final int VIEW_TYPE_DEFAULT = 10;
    private static final int VIEW_SOCIAL_SHARE = 11;

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public DashboardHomeTabAdapter(Context context, ArrayList<HomeTabDataListModel> items, DashboardHomeTabNavigator listner) {
        super();
        mContext = context;
        feedItems = items;
        this.mNavigator = listner;
    }

    @Override
    public int getItemViewType(int position) {
        HomeTabDataListModel model = feedItems.get(position);
        model.adapterPosition = position;

        switch (model.type) {
            case Constants.HomeTabSections.BDAY:
                return VIEW_TYPE_BDAY;
            case Constants.HomeTabSections.EVENT:
                return VIEW_TYPE_EVENTS;
            case Constants.HomeTabSections.SPECIAL_EVENT:
                return VIEW_TYPE_SPECIALEVENT;
            case Constants.HomeTabSections.SUNDAY:
                return VIEW_TYPE_SUNDAY;
            case Constants.HomeTabSections.HOLIDAY:
                return VIEW_TYPE_HOLIDAY;
            case Constants.HomeTabSections.NEWS_NOTICE:
                return VIEW_TYPE_NEWNOTICE;
            case Constants.HomeTabSections.MESSAGE:
                return VIEW_TYPE_ANNOUNCEMENT;
            case Constants.HomeTabSections.PTM:
                return VIEW_TYPE_PTM;
            case Constants.HomeTabSections.SURVEY:
                return VIEW_TYPE_SURVEY;
            case Constants.HomeTabSections.SOCIAL_SHARE:
                return VIEW_SOCIAL_SHARE;
            default:
                return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BDAY) {
            return new BdayViewHolder((RowBdayMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_bday_master_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_EVENTS) {
            return new EventViewHolder((RowEventMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.row_event_master_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_SPECIALEVENT) {
            return new SpecialEventViewHolder((RowSpecialEventMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_special_event_master_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_ANNOUNCEMENT) {
            return new AnnouncementViewHolder((RowAnnouncementHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_announcement_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_SUNDAY) {
            return new SundayViewHolder((RowSundayHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_sunday_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_HOLIDAY) {
            return new HolidayViewHolder((RowHolidayMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_holiday_master_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_NEWNOTICE) {
            return new NewsNoticeViewHolder((RowNewsMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_news_master_home_tab, parent, false));

        } else if (viewType == VIEW_TYPE_PTM) {
            return new PtmViewHolder((RowPtmHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_ptm_home_tab, parent, false));
        } else if (viewType == VIEW_TYPE_SURVEY) {
            return new SurveyViewHolder((RowSurveyMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_survey_master_home_tab, parent, false));
        } else if (viewType == VIEW_SOCIAL_SHARE) {
            return new SocialShareViewHolder((RowSocialLinksHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_social_links_home_tab, parent, false));
        } else {
            return new DefaultViewHolder((RowDefaultMasterHomeTabBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.row_default_master_home_tab, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HomeTabDataListModel model = feedItems.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ANNOUNCEMENT:
                ((AnnouncementViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_EVENTS:
                ((EventViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_SPECIALEVENT:
                ((SpecialEventViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_HOLIDAY:
                ((HolidayViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_SUNDAY:
                ((SundayViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_PTM:
                ((PtmViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_NEWNOTICE:
                ((NewsNoticeViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_BDAY:
                ((BdayViewHolder) holder).bind(model);
                break;
            case VIEW_TYPE_SURVEY:
                ((SurveyViewHolder) holder).bind(model);
                break;

            case VIEW_SOCIAL_SHARE:
                ((SocialShareViewHolder) holder).bind(model);
                break;
            default:
                ((DefaultViewHolder) holder).bind(model);
                break;
        }
    }

    class BdayViewHolder extends RecyclerView.ViewHolder {

        public RowBdayMasterHomeTabBinding itemBinding;

        BdayViewHolder(RowBdayMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.bdayRecyclerView.setVisibility(View.VISIBLE);
                itemBinding.bdayRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                BdayAdapter adapter = new BdayAdapter(mContext, model.getFeed(), new BdayAdapter.BdayInterface() {
                    @Override
                    public void showFullDesc(String description, String title) {
                        mNavigator.showFullDesc(description, title);
                    }
                });
                itemBinding.bdayRecyclerView.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.bdayRecyclerView.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));
            } else {
                itemBinding.bdayRecyclerView.setVisibility(View.GONE);
                itemBinding.bdayRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        public RowEventMasterHomeTabBinding itemBinding;

        EventViewHolder(RowEventMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.eventRV.setVisibility(View.VISIBLE);

                EventAdapter adapter = new EventAdapter(mContext, model.getFeed(), new EventNavigator() {
                    @Override
                    public void checkIn(int eventId, int childAdapterPosition) {
                        mNavigator.checkIn(eventId, childAdapterPosition, model.adapterPosition);
                    }

                    @Override
                    public void rsvp(int eventId, String rsvp) {
                        mNavigator.rsvp(eventId, rsvp);
                    }

                    @Override
                    public void showFullDesc(String description, String title) {
                        mNavigator.showFullDesc(description, title);
                    }
                });
                itemBinding.eventRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.eventRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));
                itemBinding.eventRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            } else {
                itemBinding.eventRV.setVisibility(View.GONE);
                itemBinding.eventRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class SpecialEventViewHolder extends RecyclerView.ViewHolder {

        public RowSpecialEventMasterHomeTabBinding itemBinding;

        SpecialEventViewHolder(RowSpecialEventMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.specialEventRV.setVisibility(View.VISIBLE);
                itemBinding.specialEventRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                SpecialEventAdapter adapter = new SpecialEventAdapter(mContext, model.getFeed(), new SpecialEventAdapter.SpecialEventInterface() {
                    @Override
                    public void showFullDesc(String description, String title) {
                        mNavigator.showFullDesc(description, title);
                    }
                });
                itemBinding.specialEventRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.specialEventRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));
            } else {
                itemBinding.specialEventRV.setVisibility(View.GONE);
                itemBinding.specialEventRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        public RowAnnouncementHomeTabBinding itemBinding;

        AnnouncementViewHolder(RowAnnouncementHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.setViewModel(model.getFeed().get(0));
                itemBinding.announcementSection.setVisibility(View.VISIBLE);
                itemBinding.announcementSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getFeed().get(0).getDescription(), model.getFeed().get(0).getTitle());
                    }
                });
            } else {
                itemBinding.announcementSection.setVisibility(View.GONE);
                itemBinding.announcementSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class SundayViewHolder extends RecyclerView.ViewHolder {

        public RowSundayHomeTabBinding itemBinding;

        SundayViewHolder(RowSundayHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.sundayRow.setVisibility(View.VISIBLE);
                itemBinding.sundayRow.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getFeed().get(0));

                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(model.getFeed().get(0).getDescription(), model.getFeed().get(0).getSubtitle());
                    }
                });
            } else {
                itemBinding.sundayRow.setVisibility(View.GONE);
                itemBinding.sundayRow.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class HolidayViewHolder extends RecyclerView.ViewHolder {

        public RowHolidayMasterHomeTabBinding itemBinding;

        HolidayViewHolder(RowHolidayMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.holidayRV.setVisibility(View.VISIBLE);
                itemBinding.holidayRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                HolidayAdapter adapter = new HolidayAdapter(mContext, model.getFeed(), new HolidayAdapter.HolidaySectionNavigator() {
                    @Override
                    public void showFullDesc(String description, String title) {
                        mNavigator.showFullDesc(description, title);
                    }

                    @Override
                    public void onClickRemingMe(int eventId, String eventEndDate) {
                        mNavigator.setReminder(eventId, eventEndDate, model.type);


                    }
                });
                itemBinding.holidayRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.holidayRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));
            } else {
                itemBinding.holidayRV.setVisibility(View.GONE);
                itemBinding.holidayRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class NewsNoticeViewHolder extends RecyclerView.ViewHolder {

        public RowNewsMasterHomeTabBinding itemBinding;

        NewsNoticeViewHolder(RowNewsMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.newsRV.setVisibility(View.VISIBLE);
                itemBinding.newsRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                NewsAdapter adapter = new NewsAdapter(mContext, model.getFeed(), new NewsAdapter.NewsInterface() {
                    @Override
                    public void showFullDesc(String description, String title, String startDate) {
                        mNavigator.showFullDesc(description, title, startDate);
                    }
                });
                itemBinding.newsRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.newsRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));
            } else {
                itemBinding.newsRV.setVisibility(View.GONE);
                itemBinding.newsRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class PtmViewHolder extends RecyclerView.ViewHolder {

        public RowPtmHomeTabBinding itemBinding;

        PtmViewHolder(RowPtmHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.ptmRow.setVisibility(View.VISIBLE);
                itemBinding.ptmRow.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                final HomeTabItemDetail feed = model.getFeed().get(0);
                itemBinding.setViewModel(feed);

                itemBinding.bdayDesc.setText(feed.getDescription());
                itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNavigator.showFullDesc(feed.getDescription(), feed.getSubtitle());
                    }
                });

//                if (feed.getExtraKeys().getCheckedIn()) {
//                    itemBinding.checkInIcPtm.setText(mContext.getString(R.string.checkedIn));
//                    itemBinding.goingLblPtm.setEnabled(false);
//                } else {
                setCheckInListner(feed.getID(), model.adapterPosition);
//                    itemBinding.checkInIcPtm.setText(mContext.getString(R.string.checkIn));
//                    itemBinding.goingLblPtm.setEnabled(true);
//                }
                rsvp(feed.getID(), feed.getExtraKeys().getRsvp());

            } else {
                itemBinding.ptmRow.setVisibility(View.GONE);
                itemBinding.ptmRow.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setCheckInListner(final int id, final int adapterPosition) {
            itemBinding.checkInIcPtm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.checkIn(id, 0, adapterPosition);
                }
            });
        }

        private void rsvp(final int eventId, int rsvpStatus) {
            itemBinding.goingLblPtm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            itemBinding.goingLblPtm.setSelection(rsvpStatus);
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

    class SurveyViewHolder extends RecyclerView.ViewHolder {

        public RowSurveyMasterHomeTabBinding itemBinding;

        SurveyViewHolder(RowSurveyMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.surveyRV.setVisibility(View.VISIBLE);
                itemBinding.surveyRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                SurveyAdapter adapter = new SurveyAdapter(mContext, model.getFeed(), new SurveyAdapter.SurveyInterface() {
                    @Override
                    public void showFullDesc(String description, String title, String startDate) {
                        mNavigator.showFullDesc(description, title, startDate);
                    }
                });
                itemBinding.surveyRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.surveyRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));

            } else {
                itemBinding.surveyRV.setVisibility(View.GONE);
                itemBinding.surveyRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class DefaultViewHolder extends RecyclerView.ViewHolder {

        public RowDefaultMasterHomeTabBinding itemBinding;

        DefaultViewHolder(RowDefaultMasterHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.defaultRV.setVisibility(View.VISIBLE);
                itemBinding.defaultRV.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                DefaultAdapter adapter = new DefaultAdapter(mContext, model.getFeed(), new DefaultAdapter.DefaultInterface() {
                    @Override
                    public void showFullDesc(String description, String title, String startDate) {
                        mNavigator.showFullDesc(description, title, startDate);
                    }
                });
                itemBinding.defaultRV.setAdapter(adapter);

                if (model.getFeed().size() > 1)
                    itemBinding.defaultRV.addItemDecoration(new DotsIndicatorDecoration(Constants.DotsIndicator.RADIUS, Constants.DotsIndicator.PADDING,
                            Constants.DotsIndicator.HEIGHT, mContext.getResources().getColor(R.color.black3),
                            mContext.getResources().getColor(R.color.black)));

            } else {
                itemBinding.defaultRV.setVisibility(View.GONE);
                itemBinding.defaultRV.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    }

    class SocialShareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowSocialLinksHomeTabBinding itemBinding;
        private HomeTabExtraKeysModel links;

        SocialShareViewHolder(RowSocialLinksHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabDataListModel model) {
            if (model.getFeed().size() > 0) {
                itemBinding.socialLinksSection.setVisibility(View.VISIBLE);
                itemBinding.socialLinksSection.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                itemBinding.setViewModel(model.getFeed().get(0));
                links = model.getFeed().get(0).getExtraKeys();
                setListnersForButtons();

            } else {
                itemBinding.socialLinksSection.setVisibility(View.GONE);
                itemBinding.socialLinksSection.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

        private void setListnersForButtons() {
            itemBinding.fbIc.setOnClickListener(this);
            itemBinding.youtubeIc.setOnClickListener(this);
            itemBinding.galleryIc.setOnClickListener(this);

            itemBinding.shareIc.setOnClickListener(this);
            itemBinding.shareLbl.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.galleryIc:
                    mNavigator.openLinkInSystemBrowser(links.getGooglePhotosLink(), R.string.photosUrlError);
                    break;

                case R.id.fbIc:
                    mNavigator.openLinkInSystemBrowser(links.getFaceBookUrl(), R.string.fbUrlError);
                    break;

                case R.id.youtubeIc:
                    mNavigator.openBrowserInApp(links.getYouTubeUrl(), null, null, R.string.youtubeUrlError);
                    break;

                case R.id.shareIc:
                case R.id.shareLbl:
                    mNavigator.openLinkInSystemBrowser(links.getSchoolUrl(), R.string.urlError);
                    break;
            }
        }
    }
}