package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;
import java.util.List;

public class HWCommentsBean extends Bean {
    public HWCommentsModel getData() {
        return data == null ? new HWCommentsModel() : data;
    }

    @SerializedName("data")
    @Expose
    private HWCommentsModel data;
}
