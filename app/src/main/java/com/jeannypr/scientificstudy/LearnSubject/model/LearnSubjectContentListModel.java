package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearnSubjectContentListModel {

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("time")
    @Expose
    public String time;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("link")
    @Expose
    public String link;


}
