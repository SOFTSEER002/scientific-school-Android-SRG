package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class AccountExpiredBean extends Bean {
    @SerializedName("data")
    @Expose
    public AccountExpiredModel data;
}
