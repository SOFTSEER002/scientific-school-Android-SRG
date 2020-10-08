package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyClassesModel {
    @SerializedName("classId")
    @Expose
    public int ClassId;

    @SerializedName("name")
    @Expose
    public String ClassName;

    @SerializedName("totalStudent")
    @Expose
    public int TotalStudent;

    @SerializedName("teacher")
    @Expose
    public String TeacherName;

    @SerializedName("classMonitor")
    @Expose
    public String ClassMonitor;

    @SerializedName("subjectName")
    @Expose
    public String SubjectName;
}
