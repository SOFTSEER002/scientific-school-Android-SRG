package com.jeannypr.scientificstudy.Dashboard.model;

public class FeeReminderRequest {
    private int UserId;
    private int SchoolId;
    private int AcademicYearId;

    public void setFromInstallmentId(int fromInstallmentId) {
        FromInstallmentId = fromInstallmentId;
    }

    public void setToInstallmentId(int toInstallmentId) {
        ToInstallmentId = toInstallmentId;
    }

    public void setFollowUp(Boolean followUp) {
        IsFollowUp = followUp;
    }

    private int FromInstallmentId;
    private int ToInstallmentId;
    private Boolean IsFollowUp;
    private String ClassIds;
    private String Message;

    private String FromDate;

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    private String ToDate;

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }

    public void setClassIds(String classIds) {
        ClassIds = classIds;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
