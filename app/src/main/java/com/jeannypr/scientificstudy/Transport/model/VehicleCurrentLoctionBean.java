package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class VehicleCurrentLoctionBean extends Bean {

    @SerializedName("data")
    @Expose
    public VehicleCurrentLoctionModel data;
}
