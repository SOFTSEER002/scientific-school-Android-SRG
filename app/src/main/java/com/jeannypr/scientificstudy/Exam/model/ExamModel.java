package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamModel {

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("name")
    @Expose
    public String Name;
}
