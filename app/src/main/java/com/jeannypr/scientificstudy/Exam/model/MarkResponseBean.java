package com.jeannypr.scientificstudy.Exam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class MarkResponseBean extends Bean {

    public Boolean getDataAlreadyUpdatedByOther() {
        return IsDataAlreadyUpdatedByOther == null ? false : IsDataAlreadyUpdatedByOther;
    }

    public void setDataAlreadyUpdatedByOther(Boolean dataAlreadyUpdatedByOther) {
        IsDataAlreadyUpdatedByOther = dataAlreadyUpdatedByOther;
    }

    @SerializedName("data")
    @Expose
    public MarkResponseBean data;

    @SerializedName("isMarksUpdated")
    @Expose
    private Boolean IsDataAlreadyUpdatedByOther;

}
