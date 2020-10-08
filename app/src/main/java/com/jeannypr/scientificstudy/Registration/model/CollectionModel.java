package com.jeannypr.scientificstudy.Registration.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectionModel extends BaseObservable {
    @Bindable
    @SerializedName("classname")
    @Expose
    public String ClassName;

    @Bindable
    @SerializedName("totalfee")
    @Expose
    public double TotalFee;
}
