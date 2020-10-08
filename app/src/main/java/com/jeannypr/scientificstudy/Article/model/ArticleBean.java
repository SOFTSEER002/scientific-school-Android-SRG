package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class ArticleBean extends Bean {

    @SerializedName("data")
    @Expose
    public ArrayList<ArticleListModel> data;
}
