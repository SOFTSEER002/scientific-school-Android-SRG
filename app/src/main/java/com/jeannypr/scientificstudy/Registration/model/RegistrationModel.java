package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationModel {

    @SerializedName("todayRegistration")
    @Expose
    public int TodayRegistration;

    @SerializedName("todayCollection")
    @Expose
    public int TodayCollection;

    @SerializedName("totalRegistration")
    @Expose
    public int TotalRegistration;

    @SerializedName("totalRegistrationCollection")
    @Expose
    public int TotalRegCollection;

    @SerializedName("totalAdmission")
    @Expose
    public int TotalAdmission;

    @SerializedName("totalAdmissionCollection")
    @Expose
    public int TotalAdmissionCollection;
}
