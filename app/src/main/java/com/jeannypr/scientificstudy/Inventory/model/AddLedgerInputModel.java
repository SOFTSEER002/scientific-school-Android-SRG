package com.jeannypr.scientificstudy.Inventory.model;

public class AddLedgerInputModel {
    private int SchoolId;
    private int AcademicYearId;
    private String Name;
    private int GroupId;
    private String Email;
    private String Mobile;
    private int CreatedBy;

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public int getAcademicYearId() {
        return AcademicYearId == -1 ? 0 : AcademicYearId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getGroupId() {
        return GroupId == -1 ? 0 : GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getEmail() {
        return Email == null ? "" : Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile == null ? "" : Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getCreatedBy() {
        return CreatedBy == -1 ? 0 : CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }
}
