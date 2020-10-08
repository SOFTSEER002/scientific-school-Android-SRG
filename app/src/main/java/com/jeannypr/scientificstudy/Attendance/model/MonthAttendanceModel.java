package com.jeannypr.scientificstudy.Attendance.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthAttendanceModel extends BaseObservable {

    @Bindable
    public String getRollNo() {
        return String.valueOf(RollNo);
    }

    @Bindable
    public String getAbsent() {
        return String.valueOf(Absent);
    }

    @Bindable
    public String getPresent() {
        return String.valueOf(Present);
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setRollNo(int rollNo) {
        RollNo = rollNo;
    }

    public void setAbsent(int absent) {
        Absent = absent;
    }

    public void setPresent(int present) {
        Present = present;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Bindable
    @SerializedName("rollNo")
    @Expose
    public int RollNo;

    @Bindable
    @SerializedName("absent")
    @Expose
    public int Absent;

    @Bindable
    @SerializedName("present")
    @Expose
    public int Present;

    @Bindable
    @SerializedName("fullName")
    @Expose
    public String fullName;

    @SerializedName("studentId")
    @Expose
    public int StudentId;


}
