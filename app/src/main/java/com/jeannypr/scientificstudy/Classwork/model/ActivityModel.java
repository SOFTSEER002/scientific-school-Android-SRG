package com.jeannypr.scientificstudy.Classwork.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("activityType")
    @Expose
    private int ActivityType;

    @Bindable
    @SerializedName("title")
    @Expose
    public String Title;

    @Bindable
    @SerializedName("activityDate")
    @Expose
    public String ActivityDate;


    @Bindable
    @SerializedName("submissionDate")
    @Expose
    public String ActivitySubmissionDate;

    @Bindable
    @SerializedName("class")
    @Expose
    public String ClassName;

    @Bindable
    @SerializedName("subjectName")
    @Expose
    public String SubjectName;

    @Bindable
    @SerializedName("subjectTeacher")
    @Expose
    public String TeacherName;

    @Bindable
    @SerializedName("isAssignedToClass")
    @Expose
    public Boolean IsAssignedToClass;

    @Bindable
    @SerializedName("teacherEmail")
    @Expose
    private String TeacherEmail;

    public String getTeacherEmail() {
        return TeacherEmail == null ? "" : TeacherEmail;
    }

    public String getTeacherMobile() {
        return TeacherMobile == null ? "" : TeacherMobile;
    }

    @Bindable
    @SerializedName("teacherMobile")
    @Expose
    private String TeacherMobile;

    @Bindable
    public Boolean getIsAssignedToClass() {
        return IsAssignedToClass == null ? false : IsAssignedToClass;
    }

    @Bindable
    public int getActivityType() {
        return ActivityType;
    }
}
