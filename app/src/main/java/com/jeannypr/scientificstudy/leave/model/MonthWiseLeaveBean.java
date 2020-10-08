package com.jeannypr.scientificstudy.leave.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;
/*
 * Author : Babulal
 * Date :
 * MonthWiseLeaveBean
 */


public class MonthWiseLeaveBean extends Bean {
    @SerializedName("data")
    @Expose
    public List<MonthWiseLeaveModel> data;
}

