package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproveLeaveInputModel {

    public int LeaveId;
    public int LeaveStatus;
    public String Reason;
    public int ApprovedBy;
    public String LeaveStatusStr;
}
