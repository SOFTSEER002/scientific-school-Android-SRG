package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetailsModel extends BaseObservable {

    @SerializedName("rate")
    @Expose
    public double Rate;
}
