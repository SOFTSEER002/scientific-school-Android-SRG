package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayModel {
    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("date")
    @Expose
    public String DateStr;
}
