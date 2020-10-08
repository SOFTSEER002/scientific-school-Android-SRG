package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LearnSubjectContentDataModel {


    public String getChapterName() {
        return chapterName;
    }

    public ArrayList<LearnSubjectContentListModel> getDocuments() {
        return documents;
    }

    public ArrayList<LearnSubjectContentListModel> getVideos() {
        return videos;
    }

    public ArrayList<LearnSubjectContentListModel> getLinks() {
        return links;
    }

    @SerializedName("chapterName")
    @Expose
    public String chapterName;

    @SerializedName("documents")
    @Expose
    public ArrayList<LearnSubjectContentListModel> documents;

    @SerializedName("videos")
    @Expose
    public ArrayList<LearnSubjectContentListModel> videos;

    @SerializedName("links")
    @Expose
    public ArrayList<LearnSubjectContentListModel> links;


}
