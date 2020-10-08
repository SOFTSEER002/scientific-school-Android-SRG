package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffWiseAttendanceModel {
    @SerializedName("month")
    @Expose
    private String Month;

    @SerializedName("teacherId")
    @Expose
    private int TeacherId;

    @SerializedName("halfDay")
    @Expose
    private int HalfDay;

    @SerializedName("absent")
    @Expose
    private int Absent;

    @SerializedName("present")
    @Expose
    private int Present;

    @SerializedName("teacherName")
    @Expose
    private String TeacherName;

    public String getMonth() {
        return Month == null ? "" : Month;
    }

    public int getTeacherId() {
        return TeacherId == -1 ? 0 : TeacherId;
    }

    public int getHalfDay() {
        return HalfDay == -1 ? 0 : HalfDay;
    }

    public int getAbsent() {
        return Absent == -1 ? 0 : Absent;
    }

    public int getPresent() {
        return Present == -1 ? 0 : Present;
    }

    public String getTeacherName() {
        return TeacherName == null ? "" : TeacherName;
    }
}
