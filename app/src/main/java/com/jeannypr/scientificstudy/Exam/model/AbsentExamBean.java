package com.jeannypr.scientificstudy.Exam.model;

/*
 * Author : Babulal
 * Date :
 * AbsentExamBean
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class AbsentExamBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<AbsentExamModel> data;
}

