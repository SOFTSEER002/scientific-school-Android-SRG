package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentAcademicYearModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("academicYearName")
    @Expose
    public String AcademicYearName;

    @SerializedName("startDate")
    @Expose
    public String StartDate;

    @SerializedName("endDate")
    @Expose
    public String EndDate;
}
