package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryModel {
    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("countryName")
    @Expose
    public String CountryName;
}
