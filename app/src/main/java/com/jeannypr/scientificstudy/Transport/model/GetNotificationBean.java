package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;

import java.util.List;

public class GetNotificationBean {

    @SerializedName("data")
    @Expose
    public List<TransportNotificationModel> data;

    @SerializedName("total")
    @Expose
    public int total;
}
