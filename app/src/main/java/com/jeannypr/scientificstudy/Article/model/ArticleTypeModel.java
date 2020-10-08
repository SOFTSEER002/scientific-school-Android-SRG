package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleTypeModel {


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("details")
    @Expose
    public String details;

    public boolean status;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
