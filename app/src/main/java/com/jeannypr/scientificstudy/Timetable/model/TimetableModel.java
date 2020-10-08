package com.jeannypr.scientificstudy.Timetable.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimetableModel {
    @SerializedName("classPeriods")
    @Expose
    public List<DayWisePeriodsModel> classPeriodsList;


}
