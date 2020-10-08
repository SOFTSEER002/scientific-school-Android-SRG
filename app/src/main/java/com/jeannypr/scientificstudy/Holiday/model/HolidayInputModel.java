package com.jeannypr.scientificstudy.Holiday.model;

public class HolidayInputModel {
    private int id;

    public int getId() {
        return id == -1 ? 0 : id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public int getAcademicyearId() {
        return academicyearId == -1 ? 0 : academicyearId;
    }

    public void setAcademicyearId(int academicyearId) {
        this.academicyearId = academicyearId;
    }

    public int getHolidayFor() {
        return HolidayFor == -1 ? 0 : HolidayFor;
    }

    public void setHolidayFor(int holidayFor) {
        HolidayFor = holidayFor;
    }

    public String getHolidayTitle() {
        return HolidayTitle == null ? "" : HolidayTitle;
    }

    public void setHolidayTitle(String holidayTitle) {
        HolidayTitle = holidayTitle;
    }

    public int getHolidayType() {
        return HolidayType == -1 ? 0 : HolidayType;
    }

    public void setHolidayType(int holidayType) {
        HolidayType = holidayType;
    }

    public String getDescription() {
        return Description == null ? "" : Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    private int SchoolId;
    private int academicyearId;
    private int HolidayFor;
    private String HolidayTitle;
    private int HolidayType;
    private String Description;
    private String StartDate;
    private String EndDate;
}
