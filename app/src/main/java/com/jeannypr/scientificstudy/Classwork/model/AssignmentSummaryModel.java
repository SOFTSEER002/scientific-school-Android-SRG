package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentSummaryModel {
    public int getAssigned() {
        return Assigned == -1 ? 0 : Assigned;
    }

    public int getInProgress() {
        return InProgress == -1 ? 0 : InProgress;
    }

    public int getCompleted() {
        return Completed == -1 ? 0 : Completed;
    }

    @SerializedName("assigned")
    @Expose
    private int Assigned;

    @SerializedName("inProgress")
    @Expose
    private int InProgress;

    @SerializedName("completed")
    @Expose
    private int Completed;
}
