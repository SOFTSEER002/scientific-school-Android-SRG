package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class TeacherProfileBean extends Bean {

    @SerializedName("teacher")
    @Expose
    public TeacherProfileModel data;
}
