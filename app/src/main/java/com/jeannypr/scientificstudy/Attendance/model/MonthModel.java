package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthModel {
    @SerializedName("monthId")
    @Expose
    private int MonthId;

    @SerializedName("monthName")
    @Expose
    private String MonthName;

    @SerializedName("startDate")
    @Expose
    private String StartDate;

    @SerializedName("endDate")
    @Expose
    private String EndDate;

    public void setMonthId(int monthId) {
        MonthId = monthId;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getMonthId() {
        return MonthId == -1 ? 0 : MonthId;
    }

    public String getMonthName() {
        return MonthName == null ? "" : MonthName;
    }

    public String getStartDate() {
        return StartDate == null ? "" : StartDate;
    }

    public String getEndDate() {
        return EndDate == null ? "" : EndDate;
    }
}
