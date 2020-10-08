package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Classwork.model.SaveCwHwResponseModel;

public class SavePostResponseBean extends Bean {
    @SerializedName("data")
    @Expose
    public SavePostResponseModel data;
}
