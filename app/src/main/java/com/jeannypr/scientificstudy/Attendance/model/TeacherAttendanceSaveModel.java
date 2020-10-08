package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherAttendanceSaveModel {

    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;
    @SerializedName("AcademicyearId")
    @Expose
    public int AcademicyearId;
    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;
    @SerializedName("Day")
    @Expose
    public int Day;
    @SerializedName("Month")
    @Expose
    public int Month;
    @SerializedName("Year")
    @Expose
    public int Year;
    @SerializedName("AttendanceArr")
    @Expose
    public String AttendanceArr;
}
