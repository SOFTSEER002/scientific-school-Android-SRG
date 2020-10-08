package com.jeannypr.scientificstudy.Events.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class NewsNoticeDetailBean extends Bean {
    @SerializedName("news")
    @Expose
    public NewsNoticeDetailModel data;
}
