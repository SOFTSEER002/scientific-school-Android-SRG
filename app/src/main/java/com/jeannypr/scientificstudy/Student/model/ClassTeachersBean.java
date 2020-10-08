package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class ClassTeachersBean extends Bean {
    @SerializedName("classteachers")
    @Expose
    public List<ClassTeachersModel> classteachers;
}
