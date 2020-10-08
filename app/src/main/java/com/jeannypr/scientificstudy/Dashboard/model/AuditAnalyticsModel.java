package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditAnalyticsModel {
    public int getUsingApp() {
        return usingApp == -1 ? 0 : usingApp;
    }

    public void setUsingApp(int usingApp) {
        this.usingApp = usingApp;
    }

    public int getNotUsingApp() {
        return notUsingApp == -1 ? 0 : notUsingApp;
    }

    public void setNotUsingApp(int notUsingApp) {
        this.notUsingApp = notUsingApp;
    }

    public String getAnalyticsFromDate() {
        return analyticsFromDate == null ? "" : analyticsFromDate;
    }

    public void setAnalyticsFromDate(String analyticsFromDate) {
        this.analyticsFromDate = analyticsFromDate;
    }

    public String getAnalyticsToDate() {
        return analyticsToDate == null ? "" : analyticsToDate;
    }

    public void setAnalyticsToDate(String analyticsToDate) {
        this.analyticsToDate = analyticsToDate;
    }

    @SerializedName("usingApp")
    @Expose
    private int usingApp;

    @SerializedName("notUsingApp")
    @Expose
    private int notUsingApp;

    @SerializedName("analyticsFromDate")
    @Expose
    private String analyticsFromDate;

    @SerializedName("analyticsToDate")
    @Expose
    private String analyticsToDate;
}
