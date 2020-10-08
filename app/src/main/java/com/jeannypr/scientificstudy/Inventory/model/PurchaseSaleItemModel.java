package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseSaleItemModel extends BaseObservable {
    @Bindable
    @SerializedName("amount")
    @Expose
    public float Amount;

    @Bindable
    @SerializedName("voucherType")
    @Expose
    public String VoucherType;

    @SerializedName("transactionDate")
    @Expose
    public String TransactionDate;

    @SerializedName("transactionDateString")
    @Expose
    public String TransactionDateString;

    @Bindable
    @SerializedName("ledgerName")
    @Expose
    public String LedgerName;

    @Bindable
    @SerializedName("itemName")
    @Expose
    public String ItemName;

    @Bindable
    @SerializedName("quantity")
    @Expose
    public int Quantity;

    @Bindable
    @SerializedName("ratePerItem")
    @Expose
    public float RatePerItem;
}
