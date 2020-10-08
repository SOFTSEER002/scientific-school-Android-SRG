package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAttendanceModel {
    @SerializedName("studentId")
    @Expose
    private int StudentId;

    @SerializedName("isPresent")
    @Expose
    private Boolean IsPresent;

    @SerializedName("fullName")
    @Expose
    private String Name;

    @SerializedName("roleNumber")
    @Expose
    private int Roll;

    public String getFatherName() {
        return FatherName == null ? "" : FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getGender() {
        return Gender == null ? "" : Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @SerializedName("fatherName")
    @Expose
    private String FatherName;

    @SerializedName("gender")
    @Expose
    private String Gender;

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public void setPresent(Boolean present) {
        IsPresent = present;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setRoll(int roll) {
        Roll = roll;
    }

    public int getStudentId() {
        return StudentId == -1 ? 0 : StudentId;
    }

    public Boolean getPresent() {
        return IsPresent;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public int getRoll() {
        return Roll == -1 ? 0 : Roll;
    }
}
