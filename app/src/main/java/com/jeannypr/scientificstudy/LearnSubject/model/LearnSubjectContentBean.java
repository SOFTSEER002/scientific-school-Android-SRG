package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class LearnSubjectContentBean extends Bean {

    @SerializedName("data")
    @Expose
    public LearnSubjectContentDataModel data;
}
