package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SaveCwHwResponseBean extends Bean {
    @SerializedName("data")
    @Expose
    public SaveCwHwResponseModel data;
}
