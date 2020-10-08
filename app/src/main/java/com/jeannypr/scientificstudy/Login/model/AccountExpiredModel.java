package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountExpiredModel {

    @SerializedName("accountType")
    @Expose
    private String AccountType;

    @SerializedName("accountExpiryDate")
    @Expose
    private String ExpiryDate;

    @SerializedName("isAccountExpired")
    @Expose
    private boolean IsExpried;

    public String getAccountType() {
        return AccountType;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public boolean isExpried() {
        return IsExpried;
    }
}
