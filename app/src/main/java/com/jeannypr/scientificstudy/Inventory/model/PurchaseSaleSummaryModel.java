package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseSaleSummaryModel extends BaseObservable {
    @Bindable
    @SerializedName("amount")
    @Expose
    public float Amount;

    @Bindable
    @SerializedName("monthId")
    @Expose
    public int MonthId;

    @Bindable
    @SerializedName("transactionMonth")
    @Expose
    public String TransactionMonth;
}
