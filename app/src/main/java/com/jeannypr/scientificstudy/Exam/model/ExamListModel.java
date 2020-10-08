package com.jeannypr.scientificstudy.Exam.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExamListModel extends BaseObservable {
    @SerializedName("upcomingExams")
    @Expose
    public List<ExamListModel> UpcomingExams;

    @SerializedName("previousExams")
    @Expose
    public List<ExamListModel> PreviousExams;

    @SerializedName("id")
    @Expose
    public int Id;

    @Bindable
    @SerializedName("testName")
    @Expose
    public String TestName;

    @Bindable
    @SerializedName("startDate")
    @Expose
    public String StartDate;

    @Bindable
    @SerializedName("endDate")
    @Expose
    public String EndDate;
}
