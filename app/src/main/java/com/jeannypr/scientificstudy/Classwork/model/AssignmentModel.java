package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AssignmentModel {
    public AssignmentSummaryModel getSummary() {
        return Summary == null ? new AssignmentSummaryModel() : Summary;
    }

    public List<AssignmentStudentsModel> getStudents() {
        return Students == null ? new ArrayList<AssignmentStudentsModel>() : Students;
    }

    @SerializedName("summary")
    @Expose
    private AssignmentSummaryModel Summary;

    @SerializedName("students")
    @Expose
    private List<AssignmentStudentsModel> Students;
}
