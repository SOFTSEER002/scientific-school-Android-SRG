package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class UnableToLoginBean extends Bean {
    @SerializedName("data")
    @Expose
    public UnableToLoginModel data;
}
