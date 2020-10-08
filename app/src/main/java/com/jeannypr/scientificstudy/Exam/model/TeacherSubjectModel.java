package com.jeannypr.scientificstudy.Exam.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherSubjectModel extends BaseObservable {

    @Bindable
    @SerializedName("class")
    @Expose
    public String className;

    @Bindable
    @SerializedName("name")
    @Expose
    public String subjectName;

    @Bindable
    public String getClassName() {
        return className;
    }

    @Bindable
    public String getSubjectName() {
        return subjectName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

   /* public TeacherSubjectModel(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }*/

}
