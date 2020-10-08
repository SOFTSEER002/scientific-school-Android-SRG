package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class StudentAttendanceBean extends Bean {

    @SerializedName("attendance")
    @Expose
    public List<StudentAttendanceModel> data;
}
