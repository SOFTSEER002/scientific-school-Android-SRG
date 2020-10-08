package com.jeannypr.scientificstudy.Fee.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeDueModel extends BaseObservable {
    @Bindable
    @SerializedName("installmentTitle")
    @Expose
    private String InstallmentTitle;

    public String getInstallmentTitle() {
        return InstallmentTitle == null ? "" :InstallmentTitle;
    }

    public void setInstallmentTitle(String installmentTitle) {
        InstallmentTitle = installmentTitle;
    }

    public int getTotalAmountDue() {
        return TotalAmountDue == -1 ? 0 : TotalAmountDue;
    }

    public void setTotalAmountDue(int totalAmountDue) {
        TotalAmountDue = totalAmountDue;
    }

    @Bindable
    @SerializedName("totalAmountDue")
    @Expose
    private int TotalAmountDue;

    @Bindable
    public int adapterPosition;

}
