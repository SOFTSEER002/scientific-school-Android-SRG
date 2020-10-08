package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class LearnTabDataListModel extends Bean {

    @SerializedName("type")
    @Expose
    public String type;


    public LearnTabItemDetail getDetails() {
        return details == null ? new LearnTabItemDetail() : details;
    }

    public void setDetails(LearnTabItemDetail details) {
        this.details = details;
    }

    @SerializedName("detail")
    @Expose
    private LearnTabItemDetail details;
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
