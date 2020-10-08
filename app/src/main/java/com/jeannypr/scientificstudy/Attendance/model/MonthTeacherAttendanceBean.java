package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class MonthTeacherAttendanceBean extends Bean {
    @SerializedName("attendance")
    @Expose
    public List<MonthTeacherAttendanceModel> data;
}
