package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignedRouteModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    private int RouteId;

    @SerializedName("routeName")
    @Expose
    private String RouteName;

    public int getId() {
        return RouteId;
    }

    public void setId(int id) {
        RouteId = id;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }
}
