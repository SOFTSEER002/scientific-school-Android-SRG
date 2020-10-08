package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileBean extends Bean {

    @SerializedName("data")
    @Expose
    public StudentProfileModel data;

}
