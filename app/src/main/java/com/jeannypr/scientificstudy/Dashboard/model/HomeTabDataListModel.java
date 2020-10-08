package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class HomeTabDataListModel extends Bean {

    @SerializedName("type")
    @Expose
    public String type;

    public ArrayList<HomeTabItemDetail> getFeed() {
        return feed == null ? new ArrayList<HomeTabItemDetail>() : feed;
    }

    public void setFeed(ArrayList<HomeTabItemDetail> feed) {
        this.feed = feed;
    }

    @SerializedName("feed")
    @Expose
    private ArrayList<HomeTabItemDetail> feed;

    public TodayTabItemDetail getDetails() {
        return details == null ? new TodayTabItemDetail() : details;
    }

    public void setDetails(TodayTabItemDetail details) {
        this.details = details;
    }

    @SerializedName("detail")
    @Expose
    private TodayTabItemDetail details;
    public int adapterPosition;

   /* @SerializedName("priority")
    @Expose
    private int Priority;

    public int getPriority() {
        return Priority == -1 ? 0 : Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }*/

    @SerializedName("priority")
    @Expose
    private String Priority;

    public String getPriority() {
        return Priority == null ? "" : Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }
}
