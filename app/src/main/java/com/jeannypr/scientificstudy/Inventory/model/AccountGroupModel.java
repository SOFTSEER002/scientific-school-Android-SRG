package com.jeannypr.scientificstudy.Inventory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountGroupModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("title")
    @Expose
    public String Title;
}
