package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteSummaryModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("routeName")
    @Expose
    public String RouteName;

    @SerializedName("totalStudents")
    @Expose
    public String TotalStudents;
}
