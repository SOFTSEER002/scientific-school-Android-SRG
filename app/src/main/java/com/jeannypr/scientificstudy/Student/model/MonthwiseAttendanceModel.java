package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthwiseAttendanceModel {
    @SerializedName("monthId")
    @Expose
    public int MonthId;

    @SerializedName("month")
    @Expose
    public String Month;

    @SerializedName("absent")
    @Expose
    public int Absent;

    @SerializedName("present")
    @Expose
    public int Present;
}
