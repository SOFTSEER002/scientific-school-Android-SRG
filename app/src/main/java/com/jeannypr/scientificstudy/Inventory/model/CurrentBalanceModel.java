package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentBalanceModel extends BaseObservable {

    @SerializedName("ledgerId")
    @Expose
    public int LedgerId;

    @SerializedName("ledgerName")
    @Expose
    public String LedgerName;

    @SerializedName("description")
    @Expose
    public String Descripation;


    @SerializedName("credit")
    @Expose
    public float Credit;

    public float getCredit() {
        return Credit == -1 ? 0 : Credit;
    }

    public float getDebit() {
        return Debit == -1 ? 0 : Debit;
    }

    @SerializedName("debit")
    @Expose

    public float Debit;

    @SerializedName("totalBalance")
    @Expose
    public float TotalBalance;
}
