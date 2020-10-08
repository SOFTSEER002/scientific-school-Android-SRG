package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Student.model.MonthwiseAttendanceModel;

import java.util.List;

public class MonthAttendanceBean extends Bean {
    @SerializedName("attendanceData")
    @Expose
    public List<MonthAttendanceModel> data;
}
