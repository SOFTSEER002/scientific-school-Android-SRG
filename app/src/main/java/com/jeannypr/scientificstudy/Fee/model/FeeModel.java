package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeModel {

    @SerializedName("todayCollection")
    @Expose
    public int TodayCollection;

    @SerializedName("currentMonthCollection")
    @Expose
    public int MonthCollection;

    @SerializedName("currentYearCollection")
    @Expose
    public int YearCollection;

    @SerializedName("todayDiscount")
    @Expose
    public int TodayDiscount;

    @SerializedName("todayDue")
    @Expose
    public int TodayDue;

    public int getTodayDiscount() {
        return TodayDiscount;
    }

    public int getTodayDue() {
        return TodayDue;
    }

    public int getTodayCollection() {
        return TodayCollection;
    }

    public int getMonthCollection() {
        return MonthCollection;
    }

    public int getYearCollection() {
        return YearCollection;
    }

    public void setTodayCollection(int todayCollection) {
        TodayCollection = todayCollection;
    }

    public void setMonthCollection(int monthCollection) {
        MonthCollection = monthCollection;
    }

    public void setYearCollection(int yearCollection) {
        YearCollection = yearCollection;
    }

}
