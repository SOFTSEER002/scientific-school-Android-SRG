package com.jeannypr.scientificstudy.Exam.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamRoasterModel extends BaseObservable {
    @Bindable
    @SerializedName("fullMark")
    @Expose
    public String FullMark;

    @Bindable
    @SerializedName("passMark")
    @Expose
    public String PassMark;

    @Bindable
    @SerializedName("subjectName")
    @Expose
    public String SubjectName;

    @Bindable
    @SerializedName("startTime")
    @Expose
    public String StartTime;

    @Bindable
    @SerializedName("endTime")
    @Expose
    public String EndTime;

    @Bindable
    @SerializedName("date")
    @Expose
    public String ExamDate;

    @Bindable
    @SerializedName("obtainedMarks")
    @Expose
    public String ObtainedMarks;

    @Bindable
    @SerializedName("grade")
    @Expose
    public String Grade;

    @Bindable
    @SerializedName("isGrade")
    @Expose
    public boolean IsGrade;

    @Bindable
    @SerializedName("isPresent")
    @Expose
    public boolean IsPresent ;

    @Bindable
    @SerializedName("notes")
    @Expose
    public String AbsentNotes;
}
