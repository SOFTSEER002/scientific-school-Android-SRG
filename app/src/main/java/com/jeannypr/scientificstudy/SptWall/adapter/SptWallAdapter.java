package com.jeannypr.scientificstudy.SptWall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.model.SptWallModel;
import com.jeannypr.scientificstudy.databinding.RowEventBinding;
import com.jeannypr.scientificstudy.databinding.RowNewsNoticeBinding;

import java.util.ArrayList;
import java.util.List;

public class SptWallAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_NEWS_NOTICE = 1;
    private static final int VIEW_TYPE_EVENT = 2;

    private Context mContext;
    private List<SptWallModel> postList, filteredList;
    private OnClickPostListner mListner;
    private Boolean mIsAdmin;

    public SptWallAdapter(Context context, List<SptWallModel> items, OnClickPostListner listner, Boolean isAdmin) {
        super();
        mContext = context;
        postList = items;
        filteredList = items;
        mListner = listner;
        mIsAdmin = isAdmin;
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public int getItemViewType(int position) {
        SptWallModel items = filteredList.get(position);
        String postType = items.PostType.toLowerCase();

        if (postType.equals(Constants.PostType.EVENT))
            return VIEW_TYPE_EVENT;
        else
            return VIEW_TYPE_NEWS_NOTICE;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NEWS_NOTICE:
                RowNewsNoticeBinding binding;
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.row_news_notice, parent, false);
                return new NewsNoticeHolder(binding);

            default:
                RowEventBinding binding1;
                binding1 = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.row_event, parent, false);
                return new EventHolder(binding1);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (filteredList.size() > 0) {
            SptWallModel post = filteredList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_NEWS_NOTICE:
                    ((NewsNoticeHolder) holder).bind(post);
                    break;

                default:
                    if (holder != null)
                        ((EventHolder) holder).bind(post);
                    break;
            }
        }
    }

    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {
                    ArrayList<SptWallModel> tempData = new ArrayList<>();

                    for (SptWallModel row : postList) {
                        if (row.EventType.toLowerCase().contains(charString.toLowerCase())) {
                            tempData.add(row);
                        }
                    }
                    filteredList = tempData;

                } else
                    filteredList = postList;


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<SptWallModel>) filterResults.values;
                notifyDataSetChanged();

                if (filteredList.size() > 0) mListner.showSearchMsg(true);
                else mListner.showSearchMsg(false);
            }
        };
    }

    private class NewsNoticeHolder extends RecyclerView.ViewHolder {
        private RowNewsNoticeBinding itemBinding;

        NewsNoticeHolder(RowNewsNoticeBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final SptWallModel item) {
            itemBinding.setNewsNotice(item);
            if (item.AttachmentType.toLowerCase().equals(Constants.AttachmentType.IMAGE)) {
                if (item.Attachment != null && !item.Attachment.equals("")) {
                    itemBinding.attachment.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.Attachment).into(itemBinding.attachment);

                } else itemBinding.attachment.setVisibility(View.GONE);
            } else
                itemBinding.attachment.setVisibility(View.GONE);  //TODO: download & open pdf


            if (item.FeedDate == null || item.FeedDate.equals(""))
                itemBinding.startDateIcon.setVisibility(View.GONE);
            else itemBinding.startDateIcon.setVisibility(View.VISIBLE);

            if (item.Time == null || item.Time.equals(""))
                itemBinding.timeIcon.setVisibility(View.GONE);
            else itemBinding.timeIcon.setVisibility(View.VISIBLE);

          /*  if (item.PostType == null || item.PostType.equals(""))
                itemBinding.postType.setVisibility(View.GONE);
            else itemBinding.postType.setVisibility(View.VISIBLE);*/

            itemBinding.desc.setText(item.Description != null ? item.Description.trim() : "");
            itemBinding.title.setText(item.Title != null ? item.Title.trim() : "");

          /*  if (item.PostType.toLowerCase().equals(Constants.PostType.NOTICE)) {
                int colorId = mContext.getResources().getColor(R.color.red);
                itemBinding.postType.setTextColor(colorId);
            } else if (item.PostType.toLowerCase().equals(Constants.PostType.NEWS)) {
                int colorId = mContext.getResources().getColor(android.R.color.holo_green_dark);
                itemBinding.postType.setTextColor(colorId);
            }*/

            if (mIsAdmin)
                itemBinding.icEdit.setVisibility(View.VISIBLE);
            else itemBinding.icEdit.setVisibility(View.GONE);
            itemBinding.icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.onClickNewsNotice(item.Id);
                }
            });
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder {
        private RowEventBinding itemBinding;

        EventHolder(RowEventBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final SptWallModel item) {
            itemBinding.setEvent(item);

            if (item.AttachmentType.toLowerCase().equals(Constants.AttachmentType.IMAGE)) {
                if (item.Attachment != null && !item.Attachment.equals("")) {
                    Glide.with(mContext).load(item.Attachment).into(itemBinding.attachment);
                    itemBinding.attachment.setVisibility(View.VISIBLE);
                } else
                    itemBinding.attachment.setVisibility(View.GONE);

            } else
                itemBinding.attachment.setVisibility(View.GONE);

            if (item.Startdate == null || item.Startdate.equals(""))
                itemBinding.startDateIcon.setVisibility(View.GONE);
            else itemBinding.startDateIcon.setVisibility(View.VISIBLE);

            if (item.Enddate == null || item.Enddate.equals(""))
                itemBinding.endDateIcon.setVisibility(View.GONE);
            else itemBinding.endDateIcon.setVisibility(View.VISIBLE);

            if (item.Time == null || item.Time.equals(""))
                itemBinding.timeIcon.setVisibility(View.GONE);
            else itemBinding.timeIcon.setVisibility(View.VISIBLE);

          /*  if (item.PostType.equals("") || item.PostType == null)
                itemBinding.postType.setVisibility(View.GONE);
            else itemBinding.postType.setVisibility(View.VISIBLE);
*/
            itemBinding.desc.setText(item.Description != null ? item.Description.trim() : "");
            itemBinding.title.setText(item.Title != null ? item.Title.trim() : "");
            int colorId = mContext.getResources().getColor(R.color.colorPrimary);
            itemBinding.postType.setTextColor(colorId);

            if (mIsAdmin)
                itemBinding.icEdit.setVisibility(View.VISIBLE);
            else itemBinding.icEdit.setVisibility(View.GONE);
            itemBinding.icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.onClickEvent(item.Id);
                }
            });
        }
    }

    public interface OnClickPostListner {
        void onClickNewsNotice(int postId);

        void onClickEvent(int postId);

        void showSearchMsg(Boolean isDataFound);
    }
}

