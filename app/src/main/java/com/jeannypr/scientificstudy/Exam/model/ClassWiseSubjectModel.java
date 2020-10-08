package com.jeannypr.scientificstudy.Exam.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassWiseSubjectModel extends BaseObservable {

    @Bindable
    @SerializedName("teacherName")
    @Expose
    private String teacherName;

    @Bindable
    @SerializedName("subjects")
    @Expose
    private String subjectName;

    @Bindable
    public String getTeacherName() {
        return teacherName == null ? "" : teacherName;
    }

    @Bindable
    public String getSubjectName() {
        return subjectName == null ? "" : subjectName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;

    }
}
