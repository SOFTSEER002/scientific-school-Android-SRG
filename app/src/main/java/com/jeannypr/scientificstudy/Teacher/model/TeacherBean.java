package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class TeacherBean extends Bean {

    @SerializedName("teachers")
    @Expose
    public List<TeacherModel> data;
}
