package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolDetailModel {
    @SerializedName("exists")
    @Expose
    public Boolean Exists;
    @SerializedName("schoolName")
    @Expose
    public String SchoolName;
    @SerializedName("schoolKey")
    @Expose
    public String SchoolKey;
    @SerializedName("schoolLogo")
    @Expose
    public String SchoolLogo;
    @SerializedName("city")
    @Expose
    public String City;
    @SerializedName("state")
    @Expose
    public String State;

    @SerializedName("subDomain")
    @Expose
    public String SubDomain;

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
    private Boolean canSeeContactNumber;

    public Boolean getPaidVersionOfApp() {
        return isPaidVersionOfApp != null;
    }

    public void setPaidVersionOfApp(Boolean paidVersionOfApp) {
        isPaidVersionOfApp = paidVersionOfApp;
    }

    private Boolean isPaidVersionOfApp;

    public Boolean getExists() {
        return Exists;
    }

    public void setExists(Boolean exists) {
        Exists = exists;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getSchoolKey() {
        return SchoolKey;
    }

    public void setSchoolKey(String schoolKey) {
        SchoolKey = schoolKey;
    }

    public String getSchoolLogo() {
        return SchoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        SchoolLogo = schoolLogo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getSubDomain() {
        return SubDomain;
    }

    public void setSubDomain(String subDomain) {
        SubDomain = subDomain;
    }
}
