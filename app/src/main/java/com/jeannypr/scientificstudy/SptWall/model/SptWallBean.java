package com.jeannypr.scientificstudy.SptWall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SptWallBean extends Bean {
    @SerializedName("data")
    @Expose
    public SptWallModel data;
}
