package com.jeannypr.scientificstudy.Base.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassAndSectionsBean extends Bean {
    @SerializedName("class")
    @Expose
    public List<ClassAndSectionsModel> data;
}
