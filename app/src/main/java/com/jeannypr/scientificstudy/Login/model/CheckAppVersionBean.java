package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckAppVersionBean extends com.jeannypr.scientificstudy.Base.Model.Bean {
    @SerializedName("release")
    @Expose
    public CheckAppVersionModel data;
}
