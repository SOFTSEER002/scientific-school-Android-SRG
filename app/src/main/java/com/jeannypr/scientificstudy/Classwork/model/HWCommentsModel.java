package com.jeannypr.scientificstudy.Classwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HWCommentsModel {
    public List<HWCommentModel> getComments() {
        return comments == null ? new ArrayList<HWCommentModel>() : comments;
    }

    @SerializedName("comments")
    @Expose
    private List<HWCommentModel> comments;
}
