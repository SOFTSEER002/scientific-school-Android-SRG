package com.jeannypr.scientificstudy.leave.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*
 * Author : Babulal
 * Date :
 * MonthWiseLeaveModel
 */


public class MonthWiseLeaveModel extends BaseObservable {
    @Bindable
    @SerializedName("id")
    @Expose
    public int Id;

    @Bindable
    @SerializedName("teacherName")
    @Expose
    public String TeacherName;

    @Bindable
    @SerializedName("formatNo")
    @Expose
    public String FormatNo;

    @Bindable
    @SerializedName("fromDate")
    @Expose
    public String FromDate;

    @Bindable
    @SerializedName("toDate")
    @Expose
    public String ToDate;

    @Bindable
    @SerializedName("reason")
    @Expose
    public String Reason;

    @Bindable
    @SerializedName("appliedOn")
    @Expose
    public String AppliedOn;

    @Bindable
    @SerializedName("noOfDays")
    @Expose
    public String NoOFDays;

    /*public MonthWiseLeaveModel(String teacherName, String formatNo, String toDate, String reason, String appliedOn, String noOfDays) {
        TeacherName = teacherName;
        FormatNo = formatNo;
        ToDate = toDate;
        Reason = reason;
        AppliedOn = appliedOn;
        NoOFDays = noOfDays;
    }*/

    @Bindable
    public String getTeacherName() {
        return TeacherName;
    }

    @Bindable
    public String getFromDate() {
        return FromDate;
    }

    @Bindable
    public String getToDate() {
        return ToDate;
    }

    @Bindable
    public String getReason() {
        return Reason;
    }

    @Bindable
    public String getAppliedOn() {
        return AppliedOn;
    }

    @Bindable
    public String getNoOFDays() {
        return NoOFDays;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public void setAppliedOn(String appliedOn) {
        AppliedOn = appliedOn;
    }

    public void setNoOFDays(String noOFDays) {
        NoOFDays = noOFDays;
    }
}
