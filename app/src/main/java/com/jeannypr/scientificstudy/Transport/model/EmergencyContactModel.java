package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmergencyContactModel extends BaseObservable {
    @SerializedName("schoolContacts")
    @Expose
    public EmergencyContactModel schoolContacts;

    @SerializedName("emergencyContacts")
    @Expose
    public List<EmergencyContactModel> emergencyContacts;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("phoneNumber1")
    @Expose
    private String PhoneNumber1;

    @SerializedName("phoneNumber2")
    @Expose
    private String PhoneNumber2;

    public String getName() {
        return Name == null ? "" : Name;
    }

    public String getPhoneNumber1() {
        return PhoneNumber1 == null ? "" : PhoneNumber1;
    }

    public String getPhoneNumber2() {
        return PhoneNumber2 == null ? "" : PhoneNumber2;
    }
}
