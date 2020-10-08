package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassSummaryModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("totalStudent")
    @Expose
    public String TotalStudents;
}
