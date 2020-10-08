package com.jeannypr.scientificstudy.Holiday.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class HolidayBean extends Bean {
    @SerializedName("holidays")
    @Expose
    public List<HolidayModel> holidays;

    @SerializedName("data")
    @Expose
    public HolidayBean data;
}
