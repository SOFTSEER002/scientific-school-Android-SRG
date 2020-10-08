package com.jeannypr.scientificstudy.leave.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaveHistoryModel extends BaseObservable {
    @Bindable
    @SerializedName("id")
    @Expose
    public int Id;

    public int AdapterPosition;

    @SerializedName("leaveType")
    @Expose
    public String LeaveType;

    @SerializedName("acronym")
    @Expose
    public String Acronym;

    @Bindable
    @SerializedName("noOfDays")
    @Expose
    public String TotalRequestedDays;

    @Bindable
    @SerializedName("fromdate")
    @Expose
    public String StartDate;

    @Bindable
    @SerializedName("toDate")
    @Expose
    public String EndDate;

    @SerializedName("leaveStatus")
    @Expose
    public int Status;

    @Bindable
    @SerializedName("reason")
    @Expose
    public String Reason;

    @SerializedName("comment")
    @Expose
    public String ApproversNote;

    @SerializedName("requesterName")
    @Expose
    public String RequesterName;

    public ArrayList<RequestedLeaveDaysModel> getDetail() {
        return Detail;
    }

    public void setDetail(ArrayList<RequestedLeaveDaysModel> detail) {
        Detail = detail;
    }

    public ArrayList<RequestedLeaveDaysModel> Detail;

    public String getRequesterName() {
        return RequesterName != null ? RequesterName : "";
    }

    public void setRequesterName(String requesterName) {
        RequesterName = requesterName;
    }

    @Bindable
    public int getLeaveId() {
        return Id != -1 ? Id : 0;
    }

    public String getLeaveType() {
        return LeaveType != null ? LeaveType : "";
    }

    public String getAcronym() {
        return Acronym != null ? Acronym : "";
    }

    @Bindable
    public String getTotalRequestedDays() {
        return TotalRequestedDays != null ? TotalRequestedDays : "";
    }

    @Bindable
    public String getStartDate() {
        return StartDate != null ? StartDate : "";
    }

    @Bindable
    public String getEndDate() {
        return EndDate != null ? EndDate : "";
    }

    public int getStatus() {
        return Status != -1 ? Status : 0;
    }

    @Bindable
    public String getReason() {
        return Reason != null ? Reason : "";
    }

    public String getApproversNote() {
        return ApproversNote == null ? "" : ApproversNote;
    }

    @Bindable
    public void setLeaveId(int leaveId) {
        Id = leaveId;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public void setAcronym(String acronym) {
        Acronym = acronym;
    }

    @Bindable
    public void setTotalRequestedDays(String totalRequestedDays) {
        TotalRequestedDays = totalRequestedDays;
    }

    @Bindable
    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    @Bindable
    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Bindable
    public void setReason(String reason) {
        Reason = reason;
    }

    public void setApproversNote(String approversNote) {
        ApproversNote = approversNote;
    }
}
