package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamMarkDetailModel {

    @SerializedName("passMark")
    @Expose
    public float PassMarks;

    @SerializedName("totalMarks")
    @Expose
    public float FullMarks;

    public Boolean getMarking() {
        return IsMarking == null ? false : IsMarking;
    }

    public void setMarking(Boolean marking) {
        IsMarking = marking;
    }

    public Boolean getFreezed() {
        return IsFreezed == null ? false : IsFreezed;
    }

    public void setFreezed(Boolean freezed) {
        IsFreezed = freezed;
    }

    @SerializedName("isMarking")
    @Expose
    private Boolean IsMarking;

    @SerializedName("isFreezed")
    @Expose
    private Boolean IsFreezed;

    @SerializedName("studentTestDetailId")
    @Expose
    public int StudentTestDetailId;

    @SerializedName("timeStamp")
    @Expose
    public String timeStamp;


}
