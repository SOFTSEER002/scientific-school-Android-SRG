package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentMarkSaveModel {

    @SerializedName("Id")
    @Expose
    public int Id;

    @SerializedName("SchoolId")
    @Expose
    public  int SchoolId;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;

    @SerializedName("ClassId")
    @Expose
    public  int ClassId;

    @SerializedName("SubjectId")
    @Expose
    public int SubjectId;

    @SerializedName("ScheduledTestId")
    @Expose
    public  int ScheduledTestId;

    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;


    @SerializedName("TotalMarks")
    @Expose
    public float TotalMarks;

    @SerializedName("IsMarking")
    @Expose
    public Boolean IsMarking;

    @SerializedName("StudentsArr")
    @Expose
    public String StudentsArr;

    @SerializedName("timeStamp")
    @Expose
    public String TimeStamp;

}
