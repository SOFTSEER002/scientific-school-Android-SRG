package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.AbstractList;
import java.util.ArrayList;

public class StaffProfileModel {
    @SerializedName("personalDetail")
    @Expose
    public StaffProfileModel PersonalDetail;
    public int SubjectAdapterPosition;

    @SerializedName("physicalDetails")
    @Expose
    public StaffProfileModel PhysicalDetails;

    @SerializedName("proffessionalDetails")
    @Expose
    public StaffProfileModel ProffessionalDetails;

    @SerializedName("classSubjects")
    @Expose
    public ArrayList<StaffProfileModel> classSubjects;

    @SerializedName("dob")
    @Expose
    public String DateOfBrith;

    @SerializedName("phone")
    @Expose
    public String Phone;

    @SerializedName("email")
    @Expose
    public String Email;

    @SerializedName("address")
    @Expose
    public String Address;

    @SerializedName("isClassTeacher")
    @Expose
    public boolean IsClassTeacher;

    @SerializedName("className")
    @Expose
    public String className;

    @SerializedName("subject")
    @Expose
    public String Subject;

    @SerializedName("qualification")
    @Expose
    public String Qualification;

    @SerializedName("designation")
    @Expose
    public String Designation;

    @SerializedName("isTrained")
    @Expose
    public boolean IsTrained;

    @SerializedName("isTeaching")
    @Expose
    public boolean IsTeaching;

    public String getDateOfBrith() {
        return DateOfBrith == null ? "" : DateOfBrith;
    }

    public String getPhone() {
        return Phone == null ? "" : Phone;
    }

    public String getEmail() {
        return Email == null ? "" : Email;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public boolean isClassTeacher() {
        return IsClassTeacher;
    }

    public String getClassName() {
        return className == null ? "" : className;
    }

    public String getSubject() {
        return Subject == null ? "" : Subject;
    }

    public String getQualification() {
        return Qualification == null ? "" : Qualification;
    }

    public String getDesignation() {
        return Designation == null ? "" : Designation;
    }

    public boolean isTrained() {
        return IsTrained;
    }

    public boolean isTeaching() {
        return IsTeaching;
    }
}
