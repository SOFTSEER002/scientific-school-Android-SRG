package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveModel {
    @SerializedName("id")
    @Expose
    public int LeaveId;

    @SerializedName("leaveType")
    @Expose
    public String LeaveName;
}
