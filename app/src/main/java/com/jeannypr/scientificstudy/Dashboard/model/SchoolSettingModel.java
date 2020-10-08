package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolSettingModel {
    public Boolean isSelfAttendanceEnabled() {
        return IsSelfAttendanceEnabled == null ? false : IsSelfAttendanceEnabled;
    }

    public Boolean isOnlineFeeAvailable() {
        return IsOnlineFeeAvailable == null ? false : IsOnlineFeeAvailable;
    }

    @SerializedName("isSelfAttendanceEnabled")
    @Expose
    private Boolean IsSelfAttendanceEnabled;

    @SerializedName("isOnlineFeeAvailable")
    @Expose
    private Boolean IsOnlineFeeAvailable;

    public Boolean getCanParentReplyInChat() {
        return CanParentReplyInChat == null ? false : CanParentReplyInChat;
    }

    public void setCanParentReplyInChat(Boolean canParentReplyInChat) {
        CanParentReplyInChat = canParentReplyInChat;
    }

    @SerializedName("isParentCanReply")
    @Expose
    private Boolean CanParentReplyInChat;

    public Boolean getCanSeeContactNumber() {
        return canSeeContactNumber == null ? false : canSeeContactNumber;
    }

    public void setCanSeeContactNumber(Boolean canSeeContactNumber) {
        this.canSeeContactNumber = canSeeContactNumber;
    }

    @SerializedName("canSeeContactNumber")
    @Expose
    private Boolean canSeeContactNumber = false;
}

