package com.jeannypr.scientificstudy.Inventory.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountModel {
    @SerializedName("id")
    @Expose
    public int Id;
    @SerializedName("name")
    @Expose
    public String Name;

    public DiscountModel(int id, String name) {
        Id = id;
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }
}
