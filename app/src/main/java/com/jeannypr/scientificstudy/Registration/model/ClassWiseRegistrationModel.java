package com.jeannypr.scientificstudy.Registration.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassWiseRegistrationModel extends BaseObservable {

    @Bindable
    @SerializedName("className")
    @Expose
    public String ClassName;

    @Bindable
    @SerializedName("totalRegistration")
    @Expose
    public int TotalRegistration;

    @Bindable
    @SerializedName("totalPermanentAdmission")
    @Expose
    public int TotalPermanentAdmission;

    @Bindable
    @SerializedName("totalRegistrationFees")
    @Expose
    public int TotalRegistrationFees;
}
