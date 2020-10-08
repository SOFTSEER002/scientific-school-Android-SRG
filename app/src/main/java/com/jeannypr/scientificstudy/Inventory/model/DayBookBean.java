package com.jeannypr.scientificstudy.Inventory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class DayBookBean extends Bean {

    @SerializedName("data")
    @Expose
    public List<DayBookModel> data;
}
