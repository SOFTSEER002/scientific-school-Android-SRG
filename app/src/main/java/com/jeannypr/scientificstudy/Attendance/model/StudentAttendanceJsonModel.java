package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAttendanceJsonModel {
    @SerializedName("SId")
    @Expose
    private int SId;
    @SerializedName("IsPresent")
    @Expose
    private Boolean IsPresent;

    public void setSId(int SId) {
        this.SId = SId;
    }

    public void setPresent(Boolean present) {
        IsPresent = present;
    }

    public int getSId() {
        return SId == -1 ? 0 : SId;
    }

    public Boolean getPresent() {
        return IsPresent == null ? false : IsPresent;
    }
}
