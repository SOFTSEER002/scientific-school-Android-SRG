package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class AcademicYearsBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<AcademicYearsModel> data;
}
