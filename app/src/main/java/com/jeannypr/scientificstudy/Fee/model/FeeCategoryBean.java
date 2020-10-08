package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class FeeCategoryBean extends Bean {
    @SerializedName("data")
    @Expose
    public FeeCategoryModel data;


}
