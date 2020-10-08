package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestLeaveInputModel {
    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;

    @SerializedName("StudentId")
    @Expose
    public int StudentId;

    @SerializedName("ClassId")
    @Expose
    public int ClassId;

    @SerializedName("LeaveId")
    @Expose
    public int LeaveId;

    @SerializedName("AppliedBy")
    @Expose
    public int AppliedBy;

    @SerializedName("AppliedTo")
    @Expose
    public int AppliedTo;

    @SerializedName("AppliedFor")
    @Expose
    public int AppliedFor;

    @SerializedName("AcademicyearId")
    @Expose
    public int AcademicyearId;

    @SerializedName("FormatNo")
    @Expose
    public String FormatNo;

    @SerializedName("Fromdate")
    @Expose
    public String FromDate;

    @SerializedName("Todate")
    @Expose
    public String ToDate;

    @SerializedName("Reason")
    @Expose
    public String Reason;

    @SerializedName("LeaveDays")
    @Expose
    public String LeaveDays;

    @SerializedName("ApproverNote")
    @Expose
    public String ApproverNote;

    @SerializedName("Requester")
    @Expose
    public String Requester;
}
