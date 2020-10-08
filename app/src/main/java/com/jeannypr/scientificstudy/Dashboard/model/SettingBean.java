package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SettingBean extends Bean {

    @SerializedName("data")
    @Expose
    public NotificationSettingModel data;
}
