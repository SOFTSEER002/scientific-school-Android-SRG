package com.jeannypr.scientificstudy.LearnSubject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LearnSubjectSyallbusModel {

   /* public int getId() {
        return id;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public String getTitle() {
        return title;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getResourcess() {
        return resourcess;
    }
*/

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("sharedBy")
    @Expose
    public String sharedBy;


    @SerializedName("title")
    @Expose
    public String title;


    @SerializedName("attachment")
    @Expose
    public String attachment;

    @SerializedName("resourcess")
    @Expose
    public String resourcess;


}
