package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassWiseCollectionModel {
    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public void setTotalStudents(String totalStudents) {
        TotalStudents = totalStudents;
    }

    public String getTotalAmount() {
        return TotalAmount == null ? "" : TotalAmount;
    }

    public String getClassName() {
        return ClassName == null ? "" : ClassName;
    }

    public String getTotalStudents() {
        return TotalStudents == null ? "" : TotalStudents;
    }

    @SerializedName("totalAmount")
    @Expose
    public String TotalAmount;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("totalStudents")
    @Expose
    public String TotalStudents;


}
