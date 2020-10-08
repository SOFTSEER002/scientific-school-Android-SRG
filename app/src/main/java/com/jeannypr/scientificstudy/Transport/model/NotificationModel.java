package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    private int id;
    private int schoolId;
    private int academicYearId;
    private String message;
    private String notes;
    private String schoolCode;
    private String driverName;
    private String type;
    private int sentBy;

    @SerializedName("notificationId")
    @Expose
    private int notificationId;

    @SerializedName("parentAdminUserId")
    @Expose
    private String parentAdminUserId;

    public int getNotificationId() {
        return notificationId == -1 ? 0 : notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getParentAdminUserId() {
        return parentAdminUserId == null ? "" : parentAdminUserId;
    }


    public void setParentAdminUserId(String parentAdminUserId) {
        this.parentAdminUserId = parentAdminUserId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public void setAcademicYearId(int academicYearId) {
        this.academicYearId = academicYearId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSentBy(int sentBy) {
        this.sentBy = sentBy;
    }
}
