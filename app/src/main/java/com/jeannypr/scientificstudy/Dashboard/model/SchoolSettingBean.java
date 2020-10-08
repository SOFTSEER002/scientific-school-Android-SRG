package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SchoolSettingBean extends Bean {
    @SerializedName("schoolSetting")
    @Expose
    public SchoolSettingModel data;
}
