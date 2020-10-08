package com.jeannypr.scientificstudy.Student.model;

import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentModel {
    @SerializedName("studentId")
    @Expose
    public int Id;

    @SerializedName("studentName")
    @Expose
    public String Name;

    @SerializedName("admissionNo")
    @Expose
    public String AdmissionNo;

    @SerializedName("imagePath")
    @Expose
    public String ImagePath;

    @SerializedName("userId")
    @Expose
    public int UserId;

    @SerializedName("parentUserId")
    @Expose
    public int ParentUserId;

    @SerializedName("rollNo")
    @Expose
    public String RollNo;

    @SerializedName("phone")
    @Expose
    public String PhoneNo;

    @SerializedName("email")
    @Expose
    public String EmailId;

    public Boolean getCanSeeContactNumber() {
        return canSeeContactNumber == null ? false : canSeeContactNumber;
    }

    public void setCanSeeContactNumber(Boolean canSeeContactNumber) {
        this.canSeeContactNumber = canSeeContactNumber;
    }

    @SerializedName("canSeeContactNumber")
    @Expose
    private Boolean canSeeContactNumber;
}
