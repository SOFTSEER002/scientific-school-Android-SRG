package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class VehicleRouteBean extends Bean {
    @SerializedName("data")
    @Expose
    public VehicleRouteModel data;
}
