package com.jeannypr.scientificstudy.Exam.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbsentExamModel extends BaseObservable {
    @Bindable
    @SerializedName("roleNumber")
    @Expose
    private int RollNo;

    @Bindable
    @SerializedName("fullName")
    @Expose
    private String StudentName;

    @Bindable
    @SerializedName("className")
    @Expose
    private String ClassName;

    @Bindable
    @SerializedName("subjectName")
    @Expose
    private String SubjectName;

    @Bindable
    @SerializedName("scheduledTest")
    @Expose
    private String TestName;

    @Bindable
    @SerializedName("notes")
    @Expose
    private String Notes;

    @Bindable
    public int getRollNo() {
        return RollNo == -1 ? 0 : RollNo;
    }


    @Bindable
    public String getStudentName() {
        return StudentName == null ? "" : StudentName;
    }

    @Bindable
    public String getClassName() {
        return ClassName == null ? "" : ClassName;
    }

    @Bindable
    public String getSubjectName() {
        return SubjectName == null ? "" : SubjectName;
    }

    @Bindable
    public String getTestName() {
        return TestName == null ? "" : TestName;
    }

    @Bindable
    public String getNotes() {
        return Notes == null ? "" : Notes;
    }


    public void setRollNo(int rollNo) {
        RollNo = rollNo;
    }

    public void setStudentName(String studentName) {
        this.StudentName = studentName;
    }

    public void setClassName(String className) {
        this.ClassName = className;
    }

    public void setSubjectName(String subjectName) {
        this.SubjectName = subjectName;
    }

    public void setTestName(String testName) {
        this.TestName = testName;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
