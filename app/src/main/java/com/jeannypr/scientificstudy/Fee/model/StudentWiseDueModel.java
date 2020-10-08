package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Inventory.model.VoucharModel;

import java.util.List;

public class StudentWiseDueModel {

    @SerializedName("feeDue")
    @Expose
    public List<FeeDueModel> feeDue;

    @SerializedName("voucherDue")
    @Expose
    public List<VoucherDueModel>  voucherDue;
}
