package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstallmentSummaryModel {
    @SerializedName("amount")
    @Expose
    public String Amount;

    @SerializedName("total")
    @Expose
    public String Total;

    @SerializedName("title")
    @Expose
    public String Title;
}
