package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TodayTabExtraKeysModel {
    public ArrayList<FeeSummary> getFeeSummary() {
        return feeSummary;
    }

    public void setFeeSummary(ArrayList<FeeSummary> feeSummary) {
        this.feeSummary = feeSummary;
    }

    public ArrayList<RegSummaryModel> getRegAdmSummary() {
        return RegAdmSummary;
    }

    public void setRegAdmSummary(ArrayList<RegSummaryModel> regAdmSummary) {
        RegAdmSummary = regAdmSummary;
    }

    @SerializedName("feeSummary")
    @Expose
    private ArrayList<FeeSummary> feeSummary;

    @SerializedName("regAdmSummary")
    @Expose
    private ArrayList<RegSummaryModel> RegAdmSummary;

    @SerializedName("isMyTimetableEnabled")
    @Expose
    private Boolean IsMyTimetableEnabled;

    @SerializedName("isStaffTimetableEnabled")
    @Expose
    private Boolean IsStaffTimetableEnabled;

    @SerializedName("isStudentTimetableEnabled")
    @Expose
    private Boolean IsStudentTimetableEnabled;

    @SerializedName("isHWEnabled")
    @Expose
    private Boolean IsHWEnabled;

    @SerializedName("isCWEnabled")
    @Expose
    private Boolean IsCWEnabled;

    @SerializedName("isExamEnabled")
    @Expose
    private Boolean IsExamEnabled;

    @SerializedName("isTransportEnabled")
    @Expose
    private Boolean IsTransportEnabled;

    public Boolean getMyTimetableEnabled() {
        return IsMyTimetableEnabled == null ? false : IsMyTimetableEnabled;
    }

    public void setMyTimetableEnabled(Boolean myTimetableEnabled) {
        IsMyTimetableEnabled = myTimetableEnabled;
    }

    public Boolean getStaffTimetableEnabled() {
        return IsStaffTimetableEnabled == null ? false : IsStaffTimetableEnabled;
    }

    public void setStaffTimetableEnabled(Boolean staffTimetableEnabled) {
        IsStaffTimetableEnabled = staffTimetableEnabled;
    }

    public Boolean getStudentTimetableEnabled() {
        return IsStudentTimetableEnabled == null ? false : IsStudentTimetableEnabled;
    }

    public void setStudentTimetableEnabled(Boolean studentTimetableEnabled) {
        IsStudentTimetableEnabled = studentTimetableEnabled;
    }

    public Boolean getHWEnabled() {
        return IsHWEnabled == null ? false : IsHWEnabled;
    }

    public void setHWEnabled(Boolean HWEnabled) {
        IsHWEnabled = HWEnabled;
    }

    public Boolean getCWEnabled() {
        return IsCWEnabled == null ? false : IsCWEnabled;
    }

    public void setCWEnabled(Boolean CWEnabled) {
        IsCWEnabled = CWEnabled;
    }

    public Boolean getExamEnabled() {
        return IsExamEnabled == null ? false : IsExamEnabled;
    }

    public void setExamEnabled(Boolean examEnabled) {
        IsExamEnabled = examEnabled;
    }

    public Boolean getTransportEnabled() {
        return IsTransportEnabled == null ? false : IsTransportEnabled;
    }

    public void setTransportEnabled(Boolean transportEnabled) {
        IsTransportEnabled = transportEnabled;
    }

    public Boolean getInventoryEnabled() {
        return IsInventoryEnabled == null ? false : IsInventoryEnabled;
    }

    public void setInventoryEnabled(Boolean inventoryEnabled) {
        IsInventoryEnabled = inventoryEnabled;
    }

    public Boolean getAccountsEnabled() {
        return IsAccountsEnabled == null ? false : IsAccountsEnabled;
    }

    public void setAccountsEnabled(Boolean accountsEnabled) {
        IsAccountsEnabled = accountsEnabled;
    }

    public AuditAnalyticsModel getParentAnalytics() {
        return ParentAnalytics == null ? new AuditAnalyticsModel() : ParentAnalytics;
    }

    public void setParentAnalytics(AuditAnalyticsModel parentAnalytics) {
        ParentAnalytics = parentAnalytics;
    }

    public AuditAnalyticsModel getAdminAnalytics() {
        return AdminAnalytics == null ? new AuditAnalyticsModel() : AdminAnalytics;
    }

    public void setAdminAnalytics(AuditAnalyticsModel adminAnalytics) {
        AdminAnalytics = adminAnalytics;
    }

    public String getAdminContactNo() {
        return AdminContactNo == null ? "" : AdminContactNo;
    }

    public void setAdminContactNo(String adminContactNo) {
        AdminContactNo = adminContactNo;
    }

    public String getPrincipalContactNo() {
        return PrincipalContactNo == null ? "" : PrincipalContactNo;
    }

    public void setPrincipalContactNo(String principalContactNo) {
        PrincipalContactNo = principalContactNo;
    }

    @SerializedName("isInventoryEnabled")
    @Expose
    private Boolean IsInventoryEnabled;

    @SerializedName("isAccountsEnabled")
    @Expose
    private Boolean IsAccountsEnabled;

    @SerializedName("parentAnalytics")
    @Expose
    private AuditAnalyticsModel ParentAnalytics;

    @SerializedName("staffAnalytics")
    @Expose
    private AuditAnalyticsModel AdminAnalytics;

    @SerializedName("adminContactNo")
    @Expose
    private String AdminContactNo;

    @SerializedName("principalContactNo")
    @Expose
    private String PrincipalContactNo;

    @SerializedName("advancedReview")
    @Expose
    public ArrayList<ReviewModel> AdvancedReview;

    public String getButtonLabel() {
        return ButtonLabel == null ? "" : ButtonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        ButtonLabel = buttonLabel;
    }

    public String getButtonOnClickLink() {
        return ButtonOnClickLink == null ? "" : ButtonOnClickLink;
    }

    public void setButtonOnClickLink(String buttonOnClickLink) {
        ButtonOnClickLink = buttonOnClickLink;
    }

    @SerializedName("buttonLabel")
    @Expose
    private String ButtonLabel;

    @SerializedName("buttonOnClickLink")
    @Expose
    private String ButtonOnClickLink;

    @SerializedName("isFeesEnabled")
    @Expose
    private Boolean IsFeesEnabled;

    public Boolean getFeesEnabled() {
        return IsFeesEnabled == null ? false : IsFeesEnabled;
    }

    public void setFeesEnabled(Boolean feesEnabled) {
        IsFeesEnabled = feesEnabled;
    }

    public Boolean getRegistratioEnabled() {
        return IsRegistratioEnabled == null ? false : IsRegistratioEnabled;
    }

    public void setRegistratioEnabled(Boolean registratioEnabled) {
        IsRegistratioEnabled = registratioEnabled;
    }

    @SerializedName("isRegistratioEnabled")
    @Expose
    private Boolean IsRegistratioEnabled;
}
