package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentRemarkSaveModel {


    @SerializedName("SchoolId")
    @Expose
    public  int SchoolId;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;

    @SerializedName("ClassId")
    @Expose
    public  int ClassId;



    @SerializedName("TestId")
    @Expose
    public  int TestId;

    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;


    @SerializedName("Remarks")
    @Expose
    public String Remarks;

}
