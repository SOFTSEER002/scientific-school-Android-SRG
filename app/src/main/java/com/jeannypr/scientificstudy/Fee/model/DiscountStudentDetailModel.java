package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiscountStudentDetailModel {
    @SerializedName("id")
    @Expose
    public int studentId;

    @SerializedName("classId")
    @Expose
    public int ClassId;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("fullName")
    @Expose
    public String StudentName;

    @SerializedName("studentDetail")
    @Expose
    public DiscountStudentDetailModel StudentDetail;

    @SerializedName("feeStructure")
    @Expose
    public DiscountStudentDetailModel FeeStructure;

    @SerializedName("isDefined")
    @Expose
    public boolean IsDefined;

    @SerializedName("installments")
    @Expose
    public ArrayList<FeeSummaryModel> Installments;

    /*@SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("feeinstallmentId")
    @Expose
    public int FeeinstallmentId;

    @SerializedName("feeinstallmentNo")
    @Expose
    public int FeeinstallmentNo;

    @SerializedName("totalAmountPayable")
    @Expose
    public int TotalAmountPayable;

    @SerializedName("totalAmountPaid")
    @Expose
    public int TotalAmountPaid;

    @SerializedName("totalAmountDue")
    @Expose
    public int TotalAmountDue;

    @SerializedName("isPaid")
    @Expose
    public boolean IsPaid;

    @SerializedName("isAdmissionInstallment")
    @Expose
    public boolean IsAdmissionInstallment;

    @SerializedName("installmentTitle")
    @Expose
    public String InstallmentTitle;

    @SerializedName("startDate")
    @Expose
    public String InstallmentDate;

    @SerializedName("lastPaymentDate")
    @Expose
    public String DueDate;

    @SerializedName("paymentStatus")
    @Expose
    public String PaymentStatus;*/

}
