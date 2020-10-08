package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class ClassWiseDuesBean extends Bean {

    @SerializedName("data")
    @Expose
    public List<ClassWiseDuesModel> data;
}
