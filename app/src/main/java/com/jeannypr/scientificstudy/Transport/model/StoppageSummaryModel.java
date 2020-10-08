package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoppageSummaryModel {

    @SerializedName("placeName")
    @Expose
    public String PlaceName;

    @SerializedName("totalStudents")
    @Expose
    public String TotalStudents;
}
