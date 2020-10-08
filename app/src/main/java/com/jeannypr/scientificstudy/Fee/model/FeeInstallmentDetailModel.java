package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeInstallmentDetailModel {
    @SerializedName("paymentStructure")
    @Expose
    public FeeInstallmentDetailModel paymentStructure;

    @SerializedName("feeType")
    @Expose
    public List<FeeInstallmentDetailModel> FeeType;

    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("amount")
    @Expose
    public Integer Amount;

    @SerializedName("amountPayable")
    @Expose
    public Integer AmountPayable;

    @SerializedName("amountPaid")
    @Expose
    public Integer AmountPaid;

    @SerializedName("amountDue")
    @Expose
    public Integer AmountDue;

    @SerializedName("discount")
    @Expose
    public Integer Discount;

    @SerializedName("isExtra")
    @Expose
    public Boolean IsExtra;

    @SerializedName("discountId")
    @Expose
    public Integer DiscountId;

    @SerializedName("feePaidAmount")
    @Expose
    public Integer FeePaidAmount;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("isNew")
    @Expose
    public Boolean IsNew;


}
