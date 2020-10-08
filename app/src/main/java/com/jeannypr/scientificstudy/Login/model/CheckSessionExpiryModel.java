package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckSessionExpiryModel {
    @SerializedName("userId")
    @Expose
    private int UserId;

    public String getUserName() {
        return UserName;
    }

    @SerializedName("userName")
    @Expose
    private String UserName;

    @SerializedName("isLoginExpired")
    @Expose
    private boolean IsLoginExpired;

    public int isCurrentAcademicYearId() {
        return CurrentAcademicYearId == -1 ? 0 : CurrentAcademicYearId;
    }

    @SerializedName("currentAcademicYearId")
    @Expose
    private int CurrentAcademicYearId;

    public int getUserId() {
        return UserId == -1 ? 0 : UserId;
    }

    public boolean isLoginExpired() {
        return IsLoginExpired;
    }


}
