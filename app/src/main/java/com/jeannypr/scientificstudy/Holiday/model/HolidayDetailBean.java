package com.jeannypr.scientificstudy.Holiday.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class HolidayDetailBean extends Bean {
    @SerializedName("holiday")
    @Expose
    public HolidayDetailModel data;
}
