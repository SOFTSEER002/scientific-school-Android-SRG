package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcademicYearsModel {
    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAcademicYearName() {
        return AcademicYearName == null ? "" : AcademicYearName;
    }

    public void setAcademicYearName(String academicYearName) {
        AcademicYearName = academicYearName;
    }

    @SerializedName("id")
    @Expose
    private int Id;

    @SerializedName("academicYearName")
    @Expose
    private String AcademicYearName;
}
