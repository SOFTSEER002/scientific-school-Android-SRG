package com.jeannypr.scientificstudy.Syllabus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class SyllabusBean extends Bean {
    @SerializedName("data")
    @Expose
    public ArrayList<SyllabusResponse> data;
}
