package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class TransportBean extends Bean {

    @SerializedName("transport")
    @Expose
    public TransportModel data;
}
