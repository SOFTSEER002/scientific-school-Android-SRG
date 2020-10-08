package com.jeannypr.scientificstudy.leave.model;
//Created by babulal
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthLeaveSummaryModel extends BaseObservable {
    @Bindable
    @SerializedName("staffName")
    @Expose
    public String StaffName;

    @Bindable
    @SerializedName("leaveDays")
    @Expose
    public String LeaveDays;

    @Bindable
    @SerializedName("leaveDate")
    @Expose
    public String LeaveDate;

    @Bindable
    @SerializedName("monthId")
    @Expose
    public String MonthId;

    public MonthLeaveSummaryModel(String staffName, String leaveDays, String leaveDate, String monthId) {
        StaffName = staffName;
        LeaveDays = leaveDays;
        LeaveDate = leaveDate;
        MonthId = monthId;
    }

    @Bindable
    public String getStaffName() {
        return StaffName;
    }

    @Bindable
    public String getLeaveDays() {
        return LeaveDays;
    }

    @Bindable
    public String getLeaveDate() {
        return LeaveDate;
    }

    @Bindable
    public String getMonthId() {
        return MonthId;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public void setLeaveDays(String leaveDays) {
        LeaveDays = leaveDays;
    }

    public void setLeaveDate(String leaveDate) {
        LeaveDate = leaveDate;
    }

    public void setMonthId(String monthId) {
        MonthId = monthId;
    }
}
