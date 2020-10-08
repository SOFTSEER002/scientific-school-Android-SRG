package com.jeannypr.scientificstudy.Timetable.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class TimetableBean extends Bean {

    @SerializedName("data")
    @Expose
    public TimetableModel data;
}
