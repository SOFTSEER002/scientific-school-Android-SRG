package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    public String getLabel() {
        return Label == null ? "" : Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getValue() {
        return Value == null ? "" : Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @SerializedName("label")
    @Expose
    private String Label;

    @SerializedName("value")
    @Expose
    private String Value;
}
