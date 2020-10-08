package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeeSummaryModel {
    @SerializedName("installments")
    @Expose
    public List<FeeSummaryModel> installments;

    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("feeinstallmentId")
    @Expose
    public Integer FeeinstallmentId;

    @SerializedName("feeinstallmentNo")
    @Expose
    public Integer FeeinstallmentNo;

    @SerializedName("totalAmountPayable")
    @Expose
    public Integer TotalAmountPayable;

    @SerializedName("totalAmountPaid")
    @Expose
    public Integer TotalAmountPaid;

    @SerializedName("totalAmountDue")
    @Expose
    public Integer TotalAmountDue;

    @SerializedName("isPaid")
    @Expose
    public Boolean IsPaid;

    @SerializedName("isAdmissionInstallment")
    @Expose
    public Boolean IsAdmissionInstallment;

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
    public String PaymentStatus;
}
