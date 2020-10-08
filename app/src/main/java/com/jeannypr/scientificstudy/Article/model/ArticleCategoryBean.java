package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.ArrayList;

public class ArticleCategoryBean extends Bean {

    @SerializedName("data")
    @Expose
    public ArrayList<ArticleTypeModel> data;
}
