package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentMarkJsonModel {

    @SerializedName("Id")
    @Expose
    public  int Id;

    @SerializedName("MarksObtained")
    @Expose
    public Float MarksObtained;

    @SerializedName("Notes")
    @Expose
    public String Notes;

    @SerializedName("IsPresent")
    @Expose
    public Boolean IsPresent;

    @SerializedName("GradeId")
    @Expose
    public Integer GradeId;

    @SerializedName("Grade")
    @Expose
    public String Grade;


}
