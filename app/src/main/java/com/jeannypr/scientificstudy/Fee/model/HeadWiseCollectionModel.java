package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeadWiseCollectionModel {
    @SerializedName("headName")
    @Expose
    private String HeadName;

    @SerializedName("amount")
    @Expose
    private long Amount;

    @SerializedName("modeName")
    @Expose
    private String ModeName;

    @SerializedName("headWiseCollection")
    @Expose
    public List<HeadWiseCollectionModel> HeadWiseCollection;

    @SerializedName("modeWiseCollection")
    @Expose
    public List<HeadWiseCollectionModel> ModeWiseCollection;

    public String getHeadName() {
        return HeadName;
    }

    public long getAmount() {
        return Amount;
    }

    public String getModeName() {
        return ModeName;
    }
    /* public HeadWiseCollectionModel(String headName, String amount) {
        HeadName = headName;
        Amount = amount;
    }*/
}
