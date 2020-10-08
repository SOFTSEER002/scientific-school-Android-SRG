package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectTeacherModel {
    @SerializedName("teacherId")
    @Expose
    public int TeacherId;

    @SerializedName("teacheruserId")
    @Expose
    public int TeacherUserId;

    @SerializedName("name")
    @Expose
    public String TeacherName;

    @SerializedName("subjects")
    @Expose
    public String Subjects;


}
