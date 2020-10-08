package com.jeannypr.scientificstudy.Registration.model;

import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class CollectionBean extends Bean {

    @SerializedName("")
    @Expose
    public List<CollectionModel>data;
}
