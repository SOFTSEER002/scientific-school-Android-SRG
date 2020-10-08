package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditLedgerModel extends BaseObservable {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("credit")
    @Expose
    public float Credit;

    @SerializedName("debit")
    @Expose
    public float Debit;
}
