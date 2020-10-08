package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherAttendanceJsonModel {
    @SerializedName("Id")
    @Expose
    public int Id;

    @SerializedName("AttendanceNew")
    @Expose
    public int Attendance;

    @SerializedName("OutDoor")
    @Expose
    public Boolean OutDoor;

    @SerializedName("IsExtra")
    @Expose
    public Boolean IsExtra;

    @SerializedName("Notes")
    @Expose
    public String Notes;
}
