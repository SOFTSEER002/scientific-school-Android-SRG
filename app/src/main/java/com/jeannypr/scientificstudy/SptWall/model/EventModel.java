package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventModel {
    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("eventTitle")
    @Expose
    public String Title;

    @SerializedName("instruction")
    @Expose
    public String Instruction;

    @SerializedName("startdate")
    @Expose
    public String Startdate;

    @SerializedName("enddate")
    @Expose
    public String Enddate;

    @SerializedName("eventLevel")
    @Expose
    public String EventLevel;

    @SerializedName("time")
    @Expose
    public String Time;

    @SerializedName("eventLevelString")
    @Expose
    public String EventLevelString;

    @SerializedName("isPublished")
    @Expose
    public Boolean IsPublished;

    @SerializedName("filename")
    @Expose
    public String Filename;

    @SerializedName("attachmentName")
    @Expose
    public String AttachmentName;

    @SerializedName("fileUrl")
    @Expose
    public String FileUrl;

    @SerializedName("eventVenue")
    @Expose
    public String EventVenue;
}
