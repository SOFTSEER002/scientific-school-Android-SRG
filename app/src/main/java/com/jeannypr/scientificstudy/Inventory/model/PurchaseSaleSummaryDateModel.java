package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseSaleSummaryDateModel extends BaseObservable {
    @Bindable
    @SerializedName("amount")
    @Expose
    public float Amount;

    @SerializedName("transactionDate")
    @Expose
    public String TransactionDate;

    @Bindable
    @SerializedName("transactionDateString")
    @Expose
    public String TransactionDateString;
}
