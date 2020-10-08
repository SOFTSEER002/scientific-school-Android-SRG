package com.jeannypr.scientificstudy.Student.model;

public class StudentInputModel {
    private int Id;
    private int SchoolId;
    private int AcademicYearId;
    private int UserId;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String FatherFirstName;
    private String AdmissionNumber;
    private String FatherPhone;
    private int StudentStatus;
    private int ClassId;
    private int ParentId;

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
    }

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

    public int getUserId() {
        return UserId == -1 ? 0 : UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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

    public String getFatherFirstName() {
        return FatherFirstName == null ? "" : FatherFirstName;
    }

    public void setFatherFirstName(String fatherFirstName) {
        FatherFirstName = fatherFirstName;
    }

    public String getFatherMobile() {
        return FatherPhone == null ? "" : FatherPhone;
    }

    public void setFatherMobile(String fatherMobile) {
        FatherPhone = fatherMobile;
    }

    public int getStudentStatus() {
        return StudentStatus == -1 ? 0 : StudentStatus;
    }

    public void setStudentStatus(int studentStatus) {
        StudentStatus = studentStatus;
    }

    public int getClassId() {
        return ClassId == -1 ? 0 : ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public String getAdmissionNumber() {
        return AdmissionNumber == null ? "" : AdmissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        AdmissionNumber = admissionNumber;
    }

    public int getParentId() {
        return ParentId == -1 ? 0 : ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }
}
