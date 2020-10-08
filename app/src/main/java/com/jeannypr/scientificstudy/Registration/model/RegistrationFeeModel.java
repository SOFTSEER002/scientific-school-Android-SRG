package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationFeeModel {
    @SerializedName("registrationFee")
    @Expose
    private String RegistrationFee;

    public String getRegistrationFee() {
        return RegistrationFee == null ? "" : RegistrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        RegistrationFee = registrationFee;
    }
}
