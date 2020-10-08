package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class MonthBean extends Bean {
    @SerializedName("data")
    @Expose
    public ArrayList<MonthModel> data;
}
