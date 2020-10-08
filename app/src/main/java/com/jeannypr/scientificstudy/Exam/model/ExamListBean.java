package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class ExamListBean extends Bean {
    @SerializedName("data")
    @Expose
    public ExamListModel data;

}
