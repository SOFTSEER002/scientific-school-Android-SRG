package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SelfAttendanceBean extends Bean {

    @SerializedName("data")
    @Expose
    public SelfAttendanceModel data;
}