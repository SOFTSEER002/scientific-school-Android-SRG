package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventTypeModel {
    @SerializedName("id")
    @Expose
    private Integer Id;

    @SerializedName("name")
    @Expose
    private String Name;


    public Integer getId() {
        return Id == -1 ? 0 : Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
