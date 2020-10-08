package com.jeannypr.scientificstudy.Inventory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class CurrentBalanceBean extends Bean {
    @SerializedName("data")
    @Expose
    public CurrentBalanceModel data;
}
