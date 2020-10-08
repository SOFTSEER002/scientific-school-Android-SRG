package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckAppVersionModel {
    @SerializedName("versionNumber")
    @Expose
    public int VersionNumber;

    @SerializedName("versionCode")
    @Expose
    public String VersionCode;

    @SerializedName("stage")
    @Expose
    public String Stage;
}
