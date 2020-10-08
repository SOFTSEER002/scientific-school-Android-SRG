package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountPermissionModel {
    @SerializedName("secretKey")
    @Expose
    public int SecretKey;

    private int UserId;
    private int StudentId;
    private int ClassId;
    private int SchoolId;
    private int AcademicYearId;

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setAcademicYearId(int academicYearId) {
        AcademicYearId = academicYearId;
    }
}
