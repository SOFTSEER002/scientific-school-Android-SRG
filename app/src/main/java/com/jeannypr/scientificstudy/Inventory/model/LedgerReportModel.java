package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LedgerReportModel extends BaseObservable {
    @SerializedName("id")
    @Expose
    public int Id;

    @Bindable
    @SerializedName("ledgerName")
    @Expose
    public String LedgerName;

    @Bindable
    @SerializedName("voucharName")
    @Expose
    public String VoucharName;

    @Bindable
    @SerializedName("voucharNo")
    @Expose
    public String VoucharNo;

    @Bindable
    @SerializedName("date")
    @Expose
    public String Date;

    @Bindable
    @SerializedName("credit")
    @Expose
    public float Credit;

    @Bindable
    @SerializedName("debit")
    @Expose
    public float Debit;

    @Bindable
    @SerializedName("note")
    @Expose
    public String Note;
}
