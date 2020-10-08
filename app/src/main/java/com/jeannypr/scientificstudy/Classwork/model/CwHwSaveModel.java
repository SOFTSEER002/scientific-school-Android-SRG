package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CwHwSaveModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("ActivityType")
    @Expose
    public int ActivityType;

    @SerializedName("Title")
    @Expose
    public String Title;

    @SerializedName("ActivitySubmissionDate")
    @Expose
    public String SubmissionDate;

    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;

    @SerializedName("SubjectId")
    @Expose
    public int SubjectId;

    @SerializedName("IsAssignedToClass")
    @Expose
    public Boolean IsAssignedToClass;

    @SerializedName("ClassIds")
    @Expose
    public String ClassIds;

    @SerializedName("ActivityItems")
    @Expose
    public String ActivityItemsArr;

    @SerializedName("SubjectName")
    @Expose
    public String SubjectName;

    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;
}
