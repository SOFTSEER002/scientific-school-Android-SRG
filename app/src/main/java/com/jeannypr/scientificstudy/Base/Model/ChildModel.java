package com.jeannypr.scientificstudy.Base.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildModel {

    @SerializedName("studentId")
    @Expose
    public int StudentId;

    @SerializedName("classId")
    @Expose
    public Integer ClassId;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("roleNumber")
    @Expose
    public Integer RoleNumber;

    @SerializedName("admissionNumber")
    @Expose
    public String AdmissionNumber;

    @SerializedName("fatherName")
    @Expose
    public String FatherName;

    @SerializedName("shiftId")
    @Expose
    public int ShiftId;

    @SerializedName("shiftName")
    @Expose
    public String ShiftNamel;

    public String getGender() {
        return Gender == null ? "" : Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @SerializedName("gender")
    @Expose
    private String Gender;

    public String getStudentImagePath() {
        return StudentImagePath == null ? "" : StudentImagePath;
    }

    public void setStudentImagePath(String studentImagePath) {
        StudentImagePath = studentImagePath;
    }

    @SerializedName("studentImagePath")
    @Expose
    private String StudentImagePath;
    public Boolean isStudent = true;
    public int adapterPosition;
}
