package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleRouteModel extends BaseObservable {

    @SerializedName("id")
    @Expose
    private int VehicleId;

    @SerializedName("routeId")
    @Expose
    private int RouteId;

    @SerializedName("vehicleNo")
    @Expose
    private String vehicleNo;

    public int getVehicleId() {
        return VehicleId == -1 ? 0 : VehicleId;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public int getRouteId() {
        return RouteId == -1 ? 0 : RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getVehicleNo() {
        return vehicleNo == null ? "" : vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
