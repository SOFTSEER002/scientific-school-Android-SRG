package com.jeannypr.scientificstudy.Dashboard.model;

public class BroadCastRequest {
    private int UserId;
    private int SchoolId;
    private int AcademicYearId;

    private String Audience;
    private String Ids;
    private String Message;

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }

    public void setAudience(String audience) {
        Audience = audience;
    }

    public void setClassIds(String classIds) {
        Ids = classIds;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setMessageSendType(String messageSendType) {
        MessageSendType = messageSendType;
    }

    private String MessageSendType;
}
