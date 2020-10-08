package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class StudentDetailBean extends Bean {

    @SerializedName("studentData")
    @Expose
    public StudentDetailModel data;

}
