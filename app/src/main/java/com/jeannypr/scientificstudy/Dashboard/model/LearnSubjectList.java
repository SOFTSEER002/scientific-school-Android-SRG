package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearnSubjectList {

    @SerializedName("subjectName")
    @Expose
    public String subjectName;

    @SerializedName("subjectId")
    @Expose
    public int subjectId;
}
