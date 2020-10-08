package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentRemarkJsonModel {

    @SerializedName("StudentId")
    @Expose
    public  int StudentId;

    @SerializedName("Remark")
    @Expose
    public String Remark;

    @SerializedName("Attendance")
    @Expose
    public String Attendance;


}
