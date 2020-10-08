package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LearnSubjectDataModel{


    @SerializedName("subjectLearningCounts")
    @Expose
    public String subjectLearningCounts;

    public String getSubjectLearningCounts() {
        return subjectLearningCounts;
    }

    public ArrayList<LearnSubjectDetailsListModel> getChapters() {
        return chapters;
    }

    @SerializedName("chapters")
    @Expose
    public ArrayList<LearnSubjectDetailsListModel> chapters;



    @SerializedName("syallbus")
    public LearnSubjectSyallbusModel syallbus;


}
