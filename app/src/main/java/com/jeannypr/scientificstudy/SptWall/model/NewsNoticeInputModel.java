package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsNoticeInputModel {
    @SerializedName("Id")
    @Expose
    public int Id;

    @SerializedName("CreatedBy")
    @Expose
    public int CreatedBy;

    @SerializedName("NewsType")
    @Expose
    public int NewsType;

    @SerializedName("audience")
    @Expose
    public int Audience;

    @SerializedName("isPublished")
    @Expose
    public boolean IsPublished;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("description")
    @Expose
    public String Description;

    @SerializedName("newsDate")
    @Expose
    public String NewsDate;

    @SerializedName("attachmentName")
    @Expose
    public String AttachmentName;

    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;
}
