package com.jeannypr.scientificstudy.Classwork.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityItemSaveModel {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("fileType")
    @Expose
    public int FileType;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("AttachmentName")
    @Expose
    public String AttachmentName;

    @SerializedName("uri")
    @Expose
    public Uri uri;

    @SerializedName("extension")
    @Expose
    public String Extension;

    @SerializedName("path")
    @Expose
    public String Path;
}
