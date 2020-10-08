package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsModel {

    @SerializedName("id")
    @Expose
    public int ItemId;

    @SerializedName("label")
    @Expose
    public String ItemName;
}
