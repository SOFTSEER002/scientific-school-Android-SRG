package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeModel {
    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("description")
    @Expose
    public String Description;

    @SerializedName("newsdate")
    @Expose
    public String Date;

    @SerializedName("audience")
    @Expose
    public String Audience;

    @SerializedName("audienceTypeString")
    @Expose
    public String AudienceTypeString;

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

    @SerializedName("createdBy")
    @Expose
    public String CreatedBy;
}
