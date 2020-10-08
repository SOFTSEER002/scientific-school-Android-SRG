package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassTeachersModel {
    @SerializedName("teacherId")
    @Expose
    public int TeacherId;

    @SerializedName("teacherName")
    @Expose
    public String TeacherName;

    @SerializedName("subjectName")
    @Expose
    public String SubjectName;

    @SerializedName("designation")
    @Expose
    public String designation;

    @SerializedName("department")
    @Expose
    public String department;
}
