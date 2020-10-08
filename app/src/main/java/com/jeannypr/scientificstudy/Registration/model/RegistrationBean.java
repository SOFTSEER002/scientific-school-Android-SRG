package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class RegistrationBean extends Bean {
    @SerializedName("data")
    @Expose
    public RegistrationModel data;
}
