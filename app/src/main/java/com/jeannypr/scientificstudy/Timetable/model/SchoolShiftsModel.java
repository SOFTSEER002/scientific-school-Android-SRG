package com.jeannypr.scientificstudy.Timetable.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolShiftsModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("shiftName")
    @Expose
    public String ShiftName;

    @SerializedName("startTime")
    @Expose
    public String StartTime;

    @SerializedName("endTime")
    @Expose
    public String EndTime;
}
