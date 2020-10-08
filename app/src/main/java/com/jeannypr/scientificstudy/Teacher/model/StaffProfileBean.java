package com.jeannypr.scientificstudy.Teacher.model;

import android.se.omapi.SEService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class StaffProfileBean extends Bean {
    @SerializedName("data")
    @Expose
    public StaffProfileModel data;
}
