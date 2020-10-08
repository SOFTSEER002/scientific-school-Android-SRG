package com.jeannypr.scientificstudy.Holiday.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("holidayTitle")
    @Expose
    public String HolidayTitle;

    @SerializedName("startdate")
    @Expose
    public String Startdate;

    @SerializedName("enddate")
    @Expose
    public String Enddate;

    @SerializedName("date")
    @Expose
    public String Date;

    @SerializedName("name")
    @Expose
    public String HolidayName;

}
