package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LearnTabExtraKeysModel {

    public ArrayList<LearnSubjectList> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<LearnSubjectList> subjects) {
        this.subjects = subjects;
    }

    @SerializedName("subjects")
    @Expose
    private ArrayList<LearnSubjectList> subjects;


}
