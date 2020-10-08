package com.jeannypr.scientificstudy.Article.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleSaveModel {
    @SerializedName("Id")
    @Expose
    public int Id;

    @SerializedName("AcademicYearId")
    @Expose
    public int AcademicYearId;

    @SerializedName("SchoolId")
    @Expose
    public int SchoolId;

    @SerializedName("SchoolCode")
    @Expose
    public String SchoolCode;

    @SerializedName("Title")
    @Expose
    public String Title;

    @SerializedName("Description")
    @Expose
    public String Description;

    @SerializedName("ImageUrl")
    @Expose
    public String ImageUrl;

    @SerializedName("VideoUrl")
    @Expose
    public String VideoUrl;

    @SerializedName("Url")
    @Expose
    public String Url;

    @SerializedName("UserId")
    @Expose
    public int UserId;

    @SerializedName("CategoryIds")
    @Expose
    public String CategoryIds;

}
