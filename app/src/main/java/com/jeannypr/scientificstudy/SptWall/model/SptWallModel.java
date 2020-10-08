package com.jeannypr.scientificstudy.SptWall.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SptWallModel extends BaseObservable{
    @SerializedName("posts")
    @Expose
    public List<SptWallModel> Posts;

    @SerializedName("id")
    @Expose
    public int Id;

    @Bindable
    @SerializedName("postedOn")
    @Expose
    public String PostedOn;

    @Bindable
    @SerializedName("title")
    @Expose
    public String Title;

    @Bindable
    @SerializedName("description")
    @Expose
    public String Description;

    @Bindable
    @SerializedName("startdate")
    @Expose
    public String Startdate;

    @Bindable
    @SerializedName("enddate")
    @Expose
    public String Enddate;

    @Bindable
    @SerializedName("feedDate")
    @Expose
    public String FeedDate;

    @Bindable
    @SerializedName("eventLevel")
    @Expose
    public String EventLevel;

    @Bindable
    @SerializedName("time")
    @Expose
    public String Time;

    @Bindable
    @SerializedName("audience")
    @Expose
    public String Audience;

    @Bindable
    @SerializedName("vanue")
    @Expose
    public String Venue;

    @Bindable
    @SerializedName("eventType")
    @Expose
    public String EventType;

    @Bindable
    @SerializedName("isPublished")
    @Expose
    public Boolean IsPublished;

    @Bindable
    @SerializedName("postType")
    @Expose
    public String PostType;

    @Bindable
    @SerializedName("attachment")
    @Expose
    public String Attachment;

    @Bindable
    @SerializedName("attachmentType")
    @Expose
    public String AttachmentType;

    @Bindable
    @SerializedName("postedBy")
    @Expose
    public String PostedBy;


}
