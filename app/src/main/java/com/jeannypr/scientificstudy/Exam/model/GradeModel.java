package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GradeModel {

    @SerializedName("id")
    @Expose
    public  int Id;

    @SerializedName("scaleId")
    @Expose
    public int ScaleId;

    @SerializedName("grade")
    @Expose
    public String Grade;

    @SerializedName("min")
    @Expose
    public float Min;

    @SerializedName("max")
    @Expose
    public float Max;



}
