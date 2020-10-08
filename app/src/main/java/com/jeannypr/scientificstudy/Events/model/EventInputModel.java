package com.jeannypr.scientificstudy.Events.model;

public class EventInputModel {
    private int Id;
    private int SchoolId;
    private int AcademicYearId;
    private int CreatedBy;
    private String EventTitle;
    private int EventType;
    private int EventLevel;
    private String EventVenue;
    private String StartDate;
    private String EndDate;
    private String StartTime;
    private String Budget;
    private Boolean IsPublished;
    private String Instruction;

    public String getAttachment() {
        return Attachment == null ? "" :Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    private String Attachment;

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public int getAcademicYearId() {
        return AcademicYearId == -1 ? 0 : AcademicYearId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }

    public int getCreatedBy() {
        return CreatedBy == -1 ? 0 : CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public Boolean isPublished() {
        return IsPublished == null ? false : IsPublished;
    }

    public void setPublished(Boolean published) {
        IsPublished = published;
    }

    public String getTitle() {
        return EventTitle == null ? "" : EventTitle;
    }

    public void setTitle(String title) {
        this.EventTitle = title;
    }

    public String getDescription() {
        return Instruction == null ? "" : Instruction;
    }

    public void setDescription(String description) {
        this.Instruction = description;
    }

    public int getEventLevel() {
        return EventLevel == -1 ? 0 : EventLevel;
    }

    public void setEventLevel(int eventLevel) {
        EventLevel = eventLevel;
    }

    public String getEventVenue() {
        return EventVenue == null ? "" : EventVenue;
    }

    public void setEventVenue(String eventVenue) {
        EventVenue = eventVenue;
    }

    public String getStartDate() {
        return StartDate == null ? "" : StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate == null ? "" : EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartTime() {
        return StartTime == null ? "" : StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getBudget() {
        return Budget == null ? "" : Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public int getEventType() {
        return EventType == -1 ? 0 : EventType;
    }

    public void setEventType(int eventType) {
        EventType = eventType;
    }
}
