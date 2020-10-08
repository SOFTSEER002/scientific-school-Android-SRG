package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraDayReportModel {


    @SerializedName("isHalfDay")
    @Expose
    private int IsHalfDay;

    @SerializedName("teacherName")
    @Expose
    private String Name;

    @SerializedName("date")

    @Expose
    private String Date;

    @SerializedName("day")
    @Expose
    private String Day;

    public int getIsHalfDay() {
        return IsHalfDay == -1 ? 0 : IsHalfDay;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public String getDate() {
        return Date == null ? "" : Date;
    }

    public String getDay() {
        return Day == null ? "" : Day;
    }
}
