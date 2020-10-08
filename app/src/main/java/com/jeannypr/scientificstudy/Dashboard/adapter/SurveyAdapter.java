package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Dashboard.model.HomeTabItemDetail;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowSurveyChildHomeTabBinding;

import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.MyViewHolder> {

    Context mContext;
    private List<HomeTabItemDetail> list;
    SurveyInterface mNavigator;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SurveyAdapter(Context context, ArrayList<HomeTabItemDetail> data, SurveyInterface listner) {
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

        RowSurveyChildHomeTabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_survey_child_home_tab, parent, false);
        return new SurveyAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeTabItemDetail headWiseCollectionModel = list.get(position);
        holder.bind(headWiseCollectionModel);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowSurveyChildHomeTabBinding itemBinding;

        MyViewHolder(RowSurveyChildHomeTabBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final HomeTabItemDetail model) {
            itemBinding.setViewModel(model);

            itemBinding.readMoreRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigator.showFullDesc(model.getDescription(), model.getSubtitle(), model.getStratDate());
                }
            });

            itemBinding.surveyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openWebView(model.getExtraKeys().getURL(), model.getTitle());
                }
            });
        }

        private void openWebView(String link, String title) {
            Intent signupIntent = new Intent(mContext, HelpActivity.class);
            signupIntent.putExtra(Constants.WEB_URL, Constants.HTTPS + UserPreference.getInstance(mContext).getSchoolData().getSubDomain() + Constants.SUBDOMAIN + link);
            signupIntent.putExtra(Constants.TITLE, title);
            signupIntent.putExtra(Constants.SUBTITLE, "");
            mContext.startActivity(signupIntent);
        }
    }

    public interface SurveyInterface {
        void showFullDesc(String description, String title, String stratDate);
    }
}