package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DayBookModel extends BaseObservable {
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

    public int getId() {
        return Id;
    }

    @Bindable
    public String getLedgerName() {
        return LedgerName;
    }

    @Bindable
    public String getVoucharName() {
        return VoucharName;
    }

    @Bindable
    public String getVoucharNo() {
        return VoucharNo;
    }

    @Bindable
    public String getDate() {
        return Date;
    }

    @Bindable
    public float getCredit() {
        return Credit;
    }

    @Bindable
    public float getDebit() {
        return Debit;
    }

    @Bindable
    public String getNote() {
        return Note;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }

    public void setVoucharName(String voucharName) {
        VoucharName = voucharName;
    }

    public void setVoucharNo(String voucharNo) {
        VoucharNo = voucharNo;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setCredit(float credit) {
        Credit = credit;
    }

    public void setDebit(float debit) {
        Debit = debit;
    }

    public void setNote(String note) {
        Note = note;
    }
}
