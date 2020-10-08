package com.jeannypr.scientificstudy.Attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAttendanceSaveModel {
    @SerializedName("ClassId")
    @Expose
    private int ClassId;

    @SerializedName("SchoolId")
    @Expose
    private int SchoolId;

    @SerializedName("AcademicyearId")
    @Expose
    private int AcademicyearId;

    @SerializedName("CreatedBy")
    @Expose
    private int CreatedBy;

    @SerializedName("Day")
    @Expose
    private int Day;

    @SerializedName("Month")
    @Expose
    private int Month;

    @SerializedName("Year")
    @Expose
    private int Year;

    @SerializedName("AttendanceArr")
    @Expose
    private String AttendanceArr;

    @SerializedName("Note")
    @Expose
    private String Note;

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setAcademicyearId(int academicyearId) {
        AcademicyearId = academicyearId;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public void setDay(int day) {
        Day = day;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setYear(int year) {
        Year = year;
    }

    public void setAttendanceArr(String attendanceArr) {
        AttendanceArr = attendanceArr;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getClassId() {
        return ClassId == -1 ? 0 : ClassId;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public int getAcademicyearId() {
        return AcademicyearId == -1 ? 0 : AcademicyearId;
    }

    public int getCreatedBy() {
        return CreatedBy == -1 ? 0 : CreatedBy;
    }

    public int getDay() {
        return Day == -1 ? 0 : Day;
    }

    public int getMonth() {
        return Month;
    }

    public int getYear() {
        return Year == -1 ? 0 : Year;
    }

    public String getAttendanceArr() {
        return AttendanceArr == null ? "" : AttendanceArr;
    }

    public String getNote() {
        return Note == null ? "" : Note;
    }
}
