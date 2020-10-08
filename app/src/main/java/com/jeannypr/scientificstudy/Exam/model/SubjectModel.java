package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectModel {

    @SerializedName("subjectId")
    @Expose
    public int Id;

    @SerializedName("name")
    @Expose
    public String Name;

}
