package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class SubjectExamBean extends Bean {

    @SerializedName("subjects")
    @Expose
    public List<SubjectModel> subjects;

    @SerializedName("scheduledTests")
    @Expose
    public List<ExamModel> exams;



}
