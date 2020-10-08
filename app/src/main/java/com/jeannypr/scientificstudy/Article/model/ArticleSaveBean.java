package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class ArticleSaveBean<T> extends Bean {

    @SerializedName("data")
    @Expose
    public T data;
}
