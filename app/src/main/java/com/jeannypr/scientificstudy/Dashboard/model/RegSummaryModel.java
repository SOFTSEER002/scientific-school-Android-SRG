package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegSummaryModel {
    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("totalRegistration")
    @Expose
    public int TotalReg;
}
