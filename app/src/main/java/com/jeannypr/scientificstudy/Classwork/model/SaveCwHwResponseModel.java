package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCwHwResponseModel {
    @SerializedName("activityId")
    @Expose
    public int ActivityId;

    @SerializedName("id")
    @Expose
    public int Id;
}

