package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearnSubjectDetailsListModel {

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("id")
    @Expose
    public int id;


}
