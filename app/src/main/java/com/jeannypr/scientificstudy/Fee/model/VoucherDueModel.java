package com.jeannypr.scientificstudy.Fee.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherDueModel extends BaseObservable{
    public String getVoucherInstallment() {
        return VoucherInstallment == null ? "" : VoucherInstallment;
    }

    public void setVoucherInstallment(String voucherInstallment) {
        VoucherInstallment = voucherInstallment;
    }

    public int getTotalAmountDue() {
        return TotalAmountDue == -1 ? 0 : TotalAmountDue;
    }

    public void setTotalAmountDue(int totalAmountDue) {
        TotalAmountDue = totalAmountDue;
    }

    @SerializedName("id")

    @Expose
    public int Id;

    @SerializedName("voucherId")
    @Expose
    public int VoucherId;

    @SerializedName("voucherInstallment")
    @Expose
    private String VoucherInstallment;

    @SerializedName("totalAmountDue")
    @Expose
    private int TotalAmountDue;

    @SerializedName("isPaid")
    @Expose
    public boolean IsPaid;

    @Bindable
    public int adapterPosition;
}
