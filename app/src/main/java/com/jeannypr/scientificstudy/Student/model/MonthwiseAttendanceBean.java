package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class MonthwiseAttendanceBean extends Bean {
    @SerializedName("attendanceData")
    @Expose
    public List<MonthwiseAttendanceModel> data;
}
