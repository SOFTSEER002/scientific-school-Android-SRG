package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAttendnaceModel {
    @SerializedName("isPresent")
    @Expose
    public boolean IsPresent;

    @SerializedName("date")
    @Expose
    public String DateStr;
}
