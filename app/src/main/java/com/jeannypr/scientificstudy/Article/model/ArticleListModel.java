package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArticleListModel {

    @SerializedName("articles")
    @Expose
    public ArrayList<ArticleListDetailsModel> articles;

    @SerializedName("categoryId")
    @Expose
    public int categoryId;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("details")
    @Expose
    public String details;

}
