package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class StudentRemarkBean extends Bean {

    @SerializedName("examRemark")
    @Expose
    public List<StudentRemarkModel> data;
}
