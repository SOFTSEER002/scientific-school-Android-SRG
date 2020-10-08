package com.jeannypr.scientificstudy.Fee.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassWiseDuesModel extends BaseObservable {
    public int getRollNo() {
        return RollNo;
    }

    public void setRollNo(int rollNo) {
        RollNo = rollNo;
    }

    public String getAdmissionNumber() {
        return AdmissionNumber == null ? "" : AdmissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        AdmissionNumber = admissionNumber;
    }

    public String getStudentName() {
        return StudentName == null ? "" : StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getFatherName() {
        return FatherName == null ? "" : FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getAmount() {
        return Amount == -1 ? 0 : Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    @SerializedName("roleNumber")
    @Expose
    public int RollNo;

    @Bindable
    @SerializedName("admissionNumber")
    @Expose
    private String AdmissionNumber;

    @Bindable
    @SerializedName("studentName")
    @Expose
    private String StudentName;

    @Bindable
    @SerializedName("parentName")
    @Expose
    private String FatherName;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @Bindable
    @SerializedName("amount")
    @Expose
    private int Amount;

    public int getParentUserId() {
        return ParentUserId == -1 ? 0 : ParentUserId;
    }

    public void setParentUserId(int parentUserId) {
        ParentUserId = parentUserId;
    }

    @SerializedName("parentUserId")
    @Expose
    private int ParentUserId;

    public String getPhone() {
        return Phone == null ? "" : Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @SerializedName("phone")
    @Expose
    private String Phone;

}
