package com.jeannypr.scientificstudy.Inventory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseLedgerModel {

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("name")
    @Expose
    public String LedgerName;
}
