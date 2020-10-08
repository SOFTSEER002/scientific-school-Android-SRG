package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class EventTypeBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<EventTypeModel> data;
}
