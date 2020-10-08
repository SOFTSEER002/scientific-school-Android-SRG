package com.jeannypr.scientificstudy.Teacher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherDetailModel {
    public int getTeacherId() {
        return TeacherId == -1 ? 0 : TeacherId;
    }

    public void setTeacherId(int teacherId) {
        TeacherId = teacherId;
    }

    public int getRole() {
        return Role == -1 ? 0 : Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getFirstName() {
        return FirstName == null ? "" : FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName == null ? "" : LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender == null ? "" : Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDateOfBirth() {
        return DateOfBirth == null ? "" : DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getMobile() {
        return Mobile == null ? "" : Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    @SerializedName("id")
    @Expose
    private int TeacherId;

    @SerializedName("role")
    @Expose
    private int Role;

    @SerializedName("firstName")
    @Expose
    private String FirstName;

    @SerializedName("lastName")
    @Expose
    private String LastName;

    @SerializedName("gender")
    @Expose
    private String Gender;

    @SerializedName("dateOfBirth")
    @Expose
    private String DateOfBirth;

    @SerializedName("mobile")
    @Expose
    private String Mobile;
}
