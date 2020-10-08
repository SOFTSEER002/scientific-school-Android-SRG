package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class HomeTabBean extends Bean {
    @SerializedName("data")
    @Expose
    public ArrayList<HomeTabDataListModel> data;
}
