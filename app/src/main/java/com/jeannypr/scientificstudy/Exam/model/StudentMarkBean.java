package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class StudentMarkBean extends Bean {

    @SerializedName("students")
    @Expose
    public List<StudentMarkModel> data;

    @SerializedName("testMark")
    @Expose
    public ExamMarkDetailModel marksDetail;

    @SerializedName("gradeDetails")
    @Expose
    public List<GradeModel> grades;

}
