package com.jeannypr.scientificstudy.Teacher.model;

public class StaffInputModel {
    private int Id;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String DateOfBirth;
    private String Phone;
    private int SchoolRoleId;
    public int SchoolId;
    public int CreatedBy;

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
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
        return Phone == null ? "" : Phone;
    }

    public void setMobile(String mobile) {
        Phone = mobile;
    }

    public int getRole() {
        return SchoolRoleId == -1 ? 0 : SchoolRoleId;
    }

    public void setRole(int role) {
        SchoolRoleId = role;
    }
}
