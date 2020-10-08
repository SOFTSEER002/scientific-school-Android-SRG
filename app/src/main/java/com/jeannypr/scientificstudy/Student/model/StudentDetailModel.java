package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Constants;

public class StudentDetailModel {


    @SerializedName("studentName")
    @Expose
    private String Name;

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getParentUserId() {
        return ParentUserId == -1 ? 0 : ParentUserId;
    }

    public void setParentUserId(int parentUserId) {
        ParentUserId = parentUserId;
    }

    public String getClassName() {
        return ClassName == null ? "" : ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getRollNo() {
        return RollNo == null ? "" : RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public int getClassId() {
        return ClassId == -1 ? 0 : ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public String getDateOfBirth() {
        return DateOfBirth == null ? "" : DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getMobileNo() {
        return MobileNo == null || MobileNo.equals(Constants.DEFAULT_MOBILE) ? "" : MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    @SerializedName("parentUserId")
    @Expose
    private int ParentUserId;

    public int getParentId() {
        return ParentId == -1 ? 0 : ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    @SerializedName("parentId")
    @Expose
    private int ParentId;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("rollNo")
    @Expose
    public String RollNo;

    @SerializedName("classId")
    @Expose
    private int ClassId;

    @SerializedName("dob")
    @Expose
    private String DateOfBirth;

    @SerializedName("fatherMobile")
    @Expose
    public String MobileNo;

    @SerializedName("fatherName")
    @Expose
    public String FatherName;

    public String getFatherName() {
        return FatherName == null ? "" : FatherName;
    }

    @SerializedName("admissionNo")
    @Expose
    public String AdmissionNo;

    @SerializedName("gender")
    @Expose
    public String Gender;

    public String getAdmissionNo() {
        return AdmissionNo == null ? "" : AdmissionNo;
    }

    public void setAdmissionNo(String amissionNo) {
        AdmissionNo = amissionNo;
    }

    public String getGender() {
        return Gender == null ? "" : Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getStudentStatus() {
        return StudentStatus == -1 ? 0 : StudentStatus;
    }

    public void setStudentStatus(int studentStatus) {
        StudentStatus = studentStatus;
    }

    @SerializedName("studentStatus")
    @Expose
    public int StudentStatus;
}
