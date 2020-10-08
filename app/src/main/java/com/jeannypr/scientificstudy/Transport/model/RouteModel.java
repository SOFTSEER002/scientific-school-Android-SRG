package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteModel extends BaseObservable {
    public int getRouteId() {
        return RouteId == -1 ? 0 : RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getRouteName() {
        return RouteName == null ? "" : RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    @Bindable
    @SerializedName("id")
    @Expose
    private int RouteId;

    @Bindable
    @SerializedName("routeName")
    @Expose
    private String RouteName;
    public int AdapterPosition;
}
