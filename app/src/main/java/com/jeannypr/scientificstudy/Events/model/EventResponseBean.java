package com.jeannypr.scientificstudy.Events.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Classwork.model.SaveCwHwResponseModel;

public class EventResponseBean extends Bean {
    @SerializedName("data")
    @Expose
    public SaveCwHwResponseModel data;
}
