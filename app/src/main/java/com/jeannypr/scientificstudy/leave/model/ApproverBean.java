package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class ApproverBean extends Bean {
    @SerializedName("data")
    @Expose
    public ApproverModel data;
}
