package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolModel {
    @SerializedName("schoolId")
    @Expose
    public Integer ID;
    @SerializedName("schoolCode")
    @Expose
    public String SchoolCode;

    @SerializedName("schoolName")
    @Expose
    public String SchoolName;
}
