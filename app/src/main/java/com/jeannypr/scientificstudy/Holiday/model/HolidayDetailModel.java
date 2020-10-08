package com.jeannypr.scientificstudy.Holiday.model;

public class HolidayDetailModel {
    private int id;

    public int getId() {
        return id == -1 ? 0 : id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHolidayFor() {
        return holidayfor == -1 ? 0 : holidayfor;
    }

    public void setHolidayFor(int holidayFor) {
        holidayfor = holidayFor;
    }

    public String getHolidayTitle() {
        return holidayTitle == null ? "" : holidayTitle;
    }

    public void setHolidayTitle(String holidayTitle) {
        holidayTitle = holidayTitle;
    }

    public int getHolidayType() {
        return holidayType == -1 ? 0 : holidayType;
    }

    public void setHolidayType(int holidayType) {
        holidayType = holidayType;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public String getStartDate() {
        return startdate == null ? "" : startdate;
    }

    public void setStartDate(String startDate) {
        startdate = startDate;
    }

    public String getEndDate() {
        return enddate == null ? "" : enddate;
    }

    public void setEndDate(String endDate) {
        enddate = endDate;
    }

    private int holidayfor;
    private String holidayTitle;
    private int holidayType;
    private String description;
    private String startdate;
    private String enddate;
}
