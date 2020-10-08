package com.jeannypr.scientificstudy.leave.model;

//Created by babulal

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class MonthLeaveSummaryBean extends Bean {
    @SerializedName(" ")
    @Expose
    public List<MonthLeaveSummaryModel> data;
}

