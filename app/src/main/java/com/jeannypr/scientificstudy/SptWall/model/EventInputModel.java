package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInputModel {

    @SerializedName("Id")
    @Expose
    public int Id;

    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;

    @SerializedName("EventType")
    @Expose
    public int EventType;

    @SerializedName("EventLevel")
    @Expose
    public int EventLevel;

    @SerializedName("isPublished")
    @Expose
    public boolean IsPublished;

    @SerializedName("EventTitle")
    @Expose
    public String Title;

    @SerializedName("Instruction")
    @Expose
    public String Description;

    @SerializedName("StartDate")
    @Expose
    public String StartDate;

    @SerializedName("EndDate")
    @Expose
    public String EndDate;

    @SerializedName("EventVenue")
    @Expose
    public String EventVenue;

    @SerializedName("StartTime")
    @Expose
    public String StartTime;

    @SerializedName("Budget")
    @Expose
    public int Budget;

    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;
}
