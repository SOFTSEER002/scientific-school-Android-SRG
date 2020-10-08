package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsolidatedDuesModel {
    @SerializedName("classId")
    @Expose
    public int ClassId;

    @SerializedName("feeTypeId")
    @Expose
    public int FeeTypeId;

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public void setFeeTypeId(int feeTypeId) {
        FeeTypeId = feeTypeId;
    }

    public void setExpected(int expected) {
        Expected = expected;
    }

    public void setPaid(int paid) {
        Paid = paid;
    }

    public void setDue(int due) {
        Due = due;
    }

    public void setFeeTitle(String feeTitle) {
        FeeTitle = feeTitle;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    @SerializedName("expected")
    @Expose
    public int Expected;

    @SerializedName("paid")
    @Expose
    public int Paid;

    @SerializedName("due")
    @Expose
    public int Due;

    @SerializedName("feeTitle")
    @Expose
    public String FeeTitle;

    public int getClassId() {
        return ClassId == -1 ? 0 : ClassId;
    }

    public int getFeeTypeId() {
        return FeeTypeId == -1 ? 0 : FeeTypeId;
    }

    public int getExpected() {
        return Expected == -1 ? 0 : Expected;
    }

    public int getPaid() {
        return Paid == -1 ? 0 : Paid;
    }

    public int getDue() {
        return Due == -1 ? 0 : Due;
    }

    public String getFeeTitle() {
        return FeeTitle == null ? "" : FeeTitle;
    }

    public String getClassName() {
        return ClassName == null ? "" : ClassName;
    }

    @SerializedName("className")
    @Expose
    public String ClassName;

}
