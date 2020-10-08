package com.jeannypr.scientificstudy.Registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegSourceModel {
    @SerializedName("id")
    @Expose
    private int Id;

    @SerializedName("title")
    @Expose
    private String Title;

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(int id) {
        Id = id;
    }
}