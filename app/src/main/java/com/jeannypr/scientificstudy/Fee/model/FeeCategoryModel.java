package com.jeannypr.scientificstudy.Fee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeCategoryModel {
    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public Boolean getDefault() {
        return IsDefault;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDefault(Boolean aDefault) {
        IsDefault = aDefault;
    }

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("isDefault")
    @Expose
    public Boolean IsDefault;

    @SerializedName("feeCategory")
    @Expose
    public List<FeeCategoryModel> FeeCategory;
}
