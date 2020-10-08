package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeTabExtraKeysModel {
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @SerializedName("signature")
    @Expose
    private String signature;

    public String getStartTime() {
        return startTime == null ? "" : startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime == null ? "" : endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("endTime")
    @Expose
    private String endTime;

    public Boolean getReminderSet() {
        return isReminderSet == null ? false : isReminderSet;
    }

    public void setReminderSet(Boolean reminderSet) {
        isReminderSet = reminderSet;
    }

    public Boolean getCheckedIn() {
        return checkedIn == null ? false : checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public int getRsvp() {
        return rsvp == -1 ? 0 : rsvp;
    }

    public void setRsvp(int rsvp) {
        this.rsvp = rsvp;
    }

    @SerializedName("isReminderSet")
    @Expose
    private Boolean isReminderSet;

    public String getReminderDate() {
        return reminderDate == null ? "" : reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    @SerializedName("reminderDate")
    @Expose
    private String reminderDate;

    @SerializedName("checkedIn")
    @Expose
    private Boolean checkedIn;

    @SerializedName("rsvp")
    @Expose
    private int rsvp;

    @SerializedName("url")
    @Expose
    private String URL;

    public String getURL() {
        return URL == null ? "" : URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAudience() {
        return Audience == null ? "" : Audience;
    }

    public void setAudience(String audience) {
        Audience = audience;
    }

    @SerializedName("audience")
    @Expose
    private String Audience;

    @SerializedName("schoolUrl")
    @Expose
    private String SchoolUrl;

    @SerializedName("faceBookUrl")
    @Expose
    private String FaceBookUrl;

    @SerializedName("youTubeUrl")
    @Expose
    private String YouTubeUrl;

    public String getSchoolUrl() {
        return SchoolUrl == null ? "" : SchoolUrl;
    }

    public void setSchoolUrl(String schoolUrl) {
        SchoolUrl = schoolUrl;
    }

    public String getFaceBookUrl() {
        return FaceBookUrl == null ? "" : FaceBookUrl;
    }

    public void setFaceBookUrl(String faceBookUrl) {
        FaceBookUrl = faceBookUrl;
    }

    public String getYouTubeUrl() {
        return YouTubeUrl == null ? "" : YouTubeUrl;
    }

    public void setYouTubeUrl(String youTubeUrl) {
        YouTubeUrl = youTubeUrl;
    }

    public String getGooglePhotosLink() {
        return GooglePhotosLink == null ? "" : GooglePhotosLink;
    }

    public void setGooglePhotosLink(String googlePhotosLink) {
        GooglePhotosLink = googlePhotosLink;
    }

    @SerializedName("googlePhotoLink")
    @Expose
    private String GooglePhotosLink;
}
