package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateWiseCollectionModel {

    @SerializedName("amount")
    @Expose
    public String Amount;

    @SerializedName("admissionNumber")
    @Expose
    public String AdmissionNumber;

    @SerializedName("studentName")
    @Expose
    public String StudentName;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("paymentDate")
    @Expose
    public String PaymentDate;

    @SerializedName("paymentMode")
    @Expose
    public String PaymentMode;
}
