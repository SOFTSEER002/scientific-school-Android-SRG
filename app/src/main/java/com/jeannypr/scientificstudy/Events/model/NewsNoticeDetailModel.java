package com.jeannypr.scientificstudy.Events.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsNoticeDetailModel {
    @SerializedName("createdBy")
    @Expose
    private int CreatedBy;

    @SerializedName("newsType")
    @Expose
    private int NewsType;

    @SerializedName("audience")
    @Expose
    private int audience;

    @SerializedName("isPublished")
    @Expose
    private int isPublished;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("date")
    @Expose
    private String newsdate;

    @SerializedName("attachmentName")
    @Expose
    private String attachmentName;

    public String getFileUrl() {
        return FileUrl == null ? "" : FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    @SerializedName("fileUrl")
    @Expose
    private String FileUrl;

    public int getCreatedBy() {
        return CreatedBy == -1 ? 0 : CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getNewsType() {
        return NewsType == -1 ? 0 : NewsType;
    }

    public void setNewsType(int newsType) {
        NewsType = newsType;
    }

    public int getAudience() {
        return audience == -1 ? 0 : audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public int getIsPublished() {
        return isPublished == -1 ? 0 : isPublished;
    }

    public void setIsPublished(int isPublished) {
        this.isPublished = isPublished;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsdate() {
        return newsdate == null ? "" : newsdate;
    }

    public void setNewsdate(String newsdate) {
        this.newsdate = newsdate;
    }

    public String getAttachmentName() {
        return attachmentName == null ? "" : attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
