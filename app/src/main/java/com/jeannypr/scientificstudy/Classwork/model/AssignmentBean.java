package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class AssignmentBean extends Bean {
    public AssignmentModel getData() {
        return data == null ? new AssignmentModel() : data;
    }

    @SerializedName("data")
    @Expose
    private AssignmentModel data;
}
