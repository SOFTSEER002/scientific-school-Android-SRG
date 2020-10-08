package com.jeannypr.scientificstudy.Timetable.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class SchoolShiftsBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<SchoolShiftsModel> data;
}
