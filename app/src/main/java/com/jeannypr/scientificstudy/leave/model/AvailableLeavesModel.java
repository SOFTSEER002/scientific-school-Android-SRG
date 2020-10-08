package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableLeavesModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("acronym")
    @Expose
    public String Acronym;

    @SerializedName("leaveType")
    @Expose
    public String LeaveType;

    @SerializedName("leaveConsumed")
    @Expose
    public Double ConsumedLeaves;

    @SerializedName("balanceLeave")
    @Expose
    public Double AvailableLeaves;

    @SerializedName("noOfLeaves")
    @Expose
    public Double TotalLeaves;

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public String getAcronym() {
        return Acronym != null ? Acronym : "";
    }

    public String getLeaveType() {
        return LeaveType != null ? LeaveType : "";
    }

    public Double getConsumedLeaves() {
        return ConsumedLeaves != null ? ConsumedLeaves : 0.0;
    }

    public Double getAvailableLeaves() {
        return AvailableLeaves != null ? AvailableLeaves : 0.0;
    }

    public Double getTotalLeaves() {
        return TotalLeaves != null ? TotalLeaves : 0.0;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setAcronym(String acronym) {
        Acronym = acronym;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public void setConsumedLeaves(Double consumedLeaves) {
        ConsumedLeaves = consumedLeaves;
    }

    public void setAvailableLeaves(Double availableLeaves) {
        AvailableLeaves = availableLeaves;
    }

    public void setTotalLeaves(Double totalLeaves) {
        TotalLeaves = totalLeaves;
    }
}
