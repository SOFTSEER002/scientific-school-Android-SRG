package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityModel {

    @SerializedName("id")
    @Expose
    public Integer Id;
    @SerializedName("cityName")
    @Expose
    public String CityName;
}
