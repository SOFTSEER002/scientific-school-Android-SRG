package com.jeannypr.scientificstudy.Events.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailModel {

    @SerializedName("eventTitle")
    @Expose
    private String Title;

    @SerializedName("startdate")
    @Expose
    private String StartDate;

    @SerializedName("enddate")
    @Expose
    private String enddate;

    @SerializedName("instruction")
    @Expose
    private String description;

    @SerializedName("eventVenue")
    @Expose
    private String EventVenue;

    @SerializedName("eventLevel")
    @Expose
    private int EventLevel;

    public int getEventType() {
        return EventType == -1 ? 0 : EventType;
    }

    public void setEventType(int eventType) {
        EventType = eventType;
    }

    @SerializedName("eventType")
    @Expose
    private int EventType;

    @SerializedName("startTime")
    @Expose
    private String EventTime;

    @SerializedName("isPublished")
    @Expose
    private Boolean IsPublished;

    @SerializedName("filename")
    @Expose
    private String attachmentName;

    @SerializedName("fileUrl")
    @Expose
    private String FileUrl;

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStartDate() {
        return StartDate == null ? "" : StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEnddate() {
        return enddate == null ? "" : enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventVenue() {
        return EventVenue == null ? "" : EventVenue;
    }

    public void setEventVenue(String eventVenue) {
        EventVenue = eventVenue;
    }

    public int getEventLevel() {
        return EventLevel == -1 ? 0 : EventLevel;
    }

    public void setEventLevel(int eventLevel) {
        EventLevel = eventLevel;
    }

    public String getEventTime() {
        return EventTime == null ? "" : EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public Boolean getIsPublished() {
        return IsPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        IsPublished = isPublished;
    }

    public String getAttachmentName() {
        return attachmentName == null ? "" : attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getFileUrl() {
        return FileUrl == null ? "" : FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }
}
