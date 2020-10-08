package com.jeannypr.scientificstudy.Exam.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentRemarkModel {

    @SerializedName("studentId")
    @Expose
    public  int Id;

    @SerializedName("fullName")
    @Expose
    public String Name;

    @SerializedName("roleNumber")
    @Expose
    public int Roll;

    @SerializedName("remark")
    @Expose
    @Nullable
    public String Remark;


    @SerializedName("attendance")
    @Expose
    public String Attendance;
}
