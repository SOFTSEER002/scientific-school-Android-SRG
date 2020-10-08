package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentStudentsModel {
    public int getStudentId() {
        return StudentId == -1 ? 0 : StudentId;
    }

    public String getHomeworkCurrentStatus() {
        return HomeworkCurrentStatus == null ? "" : HomeworkCurrentStatus;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    @SerializedName("studentId")
    @Expose
    private int StudentId;

    @SerializedName("currentStatus")
    @Expose
    private String HomeworkCurrentStatus;

    @SerializedName("name")
    @Expose
    private String Name;
    public boolean isChecked = false;
}
