package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucharModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("voucharName")
    @Expose
    public String VoucharName;

    public int getId() {
        return Id;
    }

    public String getVoucharName() {
        return VoucharName;
    }

    public int getVoucharNo() {
        return VoucharNo == -1 ? 0 : VoucharNo;
    }

    @SerializedName("voucharNo")
    @Expose
    public int VoucharNo;

    public int getReceiptNo() {
        return ReceiptNo == -1 ? 0 : ReceiptNo;
    }

    public String getMode() {
        return Mode == null ? "" : Mode;
    }

    @SerializedName("receiptNo")
    @Expose
    public int ReceiptNo;

    @SerializedName("mode")
    @Expose
    public String Mode;
}
