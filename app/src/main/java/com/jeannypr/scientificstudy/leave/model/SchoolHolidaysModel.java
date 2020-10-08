package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolHolidaysModel {
    @SerializedName("id")
    @Expose
    public String Id;

    @SerializedName("holidayTitle")
    @Expose
    public String HolidayTitle;

    @SerializedName("startDateString")
    @Expose
    public String StartDate;

    @SerializedName("endDateString")
    @Expose
    public String EndDate;
}
