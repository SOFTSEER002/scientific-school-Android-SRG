package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfAttendanceModel {

    @SerializedName("teacherName")
    @Expose
    public String TeacherName;

    @SerializedName("attendanceTime")
    @Expose
    public String AttendanceTime;

    @SerializedName("attendanceNote")
    @Expose
    public String AttendanceNote;

    @SerializedName("attendance")
    @Expose
    public String Attendance;

    @SerializedName("isOutDoor")
    @Expose
    public boolean IsOutDoor;

    @SerializedName("isExtra")
    @Expose
    public boolean IsExtra;

    @SerializedName("takenThrough")
    @Expose
    public String TakenThrough;

}
