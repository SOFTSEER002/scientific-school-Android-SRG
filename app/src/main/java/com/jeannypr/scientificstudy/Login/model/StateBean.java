package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class StateBean extends Bean {

    @SerializedName("data")
    @Expose
    public List<StateModel>  data;
}
