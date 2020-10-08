package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdmissionModel {
    @SerializedName("admissionNumber")
    @Expose
    private String admissionNo;

    public String getAdmissionNo() {
        return admissionNo == null ? "" : admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }
}
