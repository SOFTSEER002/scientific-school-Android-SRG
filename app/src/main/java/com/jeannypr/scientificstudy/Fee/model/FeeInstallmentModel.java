package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeInstallmentModel {

    public int getInstallmentNo() {
        return InstallmentNo == -1 ? 0 : InstallmentNo;
    }

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public String getStartDate() {
        return StartDate == null ? "" : StartDate;
    }

    public String getDueDate() {
        return DueDate == null ? "" : DueDate;
    }

    public int getId() {
        return Id;
    }

    public void setInstallmentNo(int installmentNo) {
        InstallmentNo = installmentNo;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public void setId(int id) {
        Id = id;
    }


    @SerializedName("installmentNo")
    @Expose
    public int InstallmentNo;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("startDate")
    @Expose
    public String StartDate;

    @SerializedName("dueDate")
    @Expose
    public String DueDate;

    @SerializedName("id")
    @Expose
    public int Id;

}
