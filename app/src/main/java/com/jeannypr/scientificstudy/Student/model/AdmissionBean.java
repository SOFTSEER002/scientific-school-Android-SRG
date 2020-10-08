package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class AdmissionBean extends Bean {
    @SerializedName("data")
    @Expose
    public AdmissionModel data;
}
