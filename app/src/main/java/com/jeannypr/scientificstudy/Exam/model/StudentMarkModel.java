package com.jeannypr.scientificstudy.Exam.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentMarkModel {

    @SerializedName("id")
    @Expose
    public  int Id;

    @SerializedName("studentName")
    @Expose
    public String Name;


    public String getCamelCaseName() {
        String camelCasedName =  Name == null ? "" : (Name.substring(0, 1).toUpperCase() + Name.substring(1).toLowerCase());
        return camelCasedName;
    }




    @SerializedName("roleNumber")
    @Expose
    public int Roll;

    @SerializedName("marksObtained")
    @Expose
    @Nullable
    public Float Marks;

    @SerializedName("notes")
    @Expose
    public String Notes;

    @SerializedName("isPresent")
    @Expose
    public Boolean IsPresent;

    @SerializedName("grade")
    @Expose
    public String Grade;

    @SerializedName("gradId")
    @Expose
    public Integer GradeId;

}
