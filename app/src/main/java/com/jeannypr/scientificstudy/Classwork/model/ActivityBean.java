package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class ActivityBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<ActivityModel> data;
}
