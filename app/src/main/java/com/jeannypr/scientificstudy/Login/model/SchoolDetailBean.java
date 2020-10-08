package com.jeannypr.scientificstudy.Login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

public class SchoolDetailBean extends Bean {
    @SerializedName("data")
    @Expose
    private SchoolDetailModel data;

    public SchoolDetailModel getData() {
        return data;
    }

    public void setData(SchoolDetailModel data) {
        this.data = data;
    }
}
