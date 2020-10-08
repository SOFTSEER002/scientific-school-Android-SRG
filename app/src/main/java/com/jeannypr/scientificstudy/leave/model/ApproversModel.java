package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproversModel {
    @SerializedName("id")
    @Expose
    public int ApproverId;

    @SerializedName("firstName")
    @Expose
    public String ApproverName;
}
