package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyContactsModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("schoolid")
    @Expose
    public int Schoolid;

    @SerializedName("emergencyTypetitle")
    @Expose
    public String EmergencyTypetitle;

    @SerializedName("mobileNumber")
    @Expose
    public String MobileNumber;

    @SerializedName("status")
    @Expose
    public int Status;

    @SerializedName("mobileNumber2")
    @Expose
    public String MobileNumber2;

    @SerializedName("phone")
    @Expose
    public String Phone;

    @SerializedName("isActive")
    @Expose
    public Boolean IsActive;

    @SerializedName("email")
    @Expose
    public String Email;
}
