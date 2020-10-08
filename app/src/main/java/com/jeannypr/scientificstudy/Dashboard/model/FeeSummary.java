package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeSummary {

    @SerializedName("monthText")
    @Expose
    public String Month;

    @SerializedName("amountPaid")
    @Expose
    public int AmountPaid;
}
