package com.jeannypr.scientificstudy.Events.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class EventDetailBean extends Bean {
    @SerializedName("event")
    @Expose
    public EventDetailModel data;
}
