package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateModel {
    @SerializedName("id")
    @Expose
    public Integer Id;

    @SerializedName("countryId")
    @Expose
    public int CountryId;

    @SerializedName("stateName")
    @Expose
    public String StateName;
}
