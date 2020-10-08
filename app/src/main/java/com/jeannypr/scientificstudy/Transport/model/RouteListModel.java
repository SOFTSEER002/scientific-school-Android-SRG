package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteListModel extends BaseObservable {
    @Bindable
    @SerializedName("routeName")
    @Expose
    private String RouteName;

    public int AdapterPosition;

    @Bindable
    @SerializedName("routeId")
    @Expose
    private int RouteId;

    @Bindable
    @SerializedName("vehicleNo")
    @Expose
    private String VehicleNumber;

    @Bindable
    @SerializedName("vehicleModel")
    @Expose
    private String VehicleModel;

    @Bindable
    @SerializedName("vehicleType")
    @Expose
    private String VehicleType;

    @Bindable
    @SerializedName("vehicleId")
    @Expose
    private int VehicleId;

    @Bindable
    @SerializedName("driverName")
    @Expose
    private String DriverName;

    @Bindable
    @SerializedName("driverMobile")
    @Expose
    private String DriverMobile;

    @Bindable
    @SerializedName("driverId")
    @Expose
    private int DriverId;

    public String getRouteName() {
        return RouteName == null ? "" : RouteName;
    }

    public int getRouteId() {
        return RouteId == -1 ? 0 : RouteId;
    }

    public String getVehicleNumber() {
        return VehicleNumber == null ? "" : VehicleNumber;
    }

    public String getVehicleModel() {
        return VehicleModel == null ? "" : VehicleModel;
    }

    public String getVehicleType() {
        return VehicleType == null ? "" : VehicleType;
    }

    public int getVehicleId() {
        return VehicleId == -1 ? 0 : VehicleId;
    }

    public String getDriverName() {
        return DriverName == null ? "" : DriverName;
    }

    public String getDriverMobile() {
        return DriverMobile == null ? "" : DriverMobile;
    }

    public int getDriverId() {
        return DriverId == -1 ? 0 : DriverId;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public void setAdapterPosition(int adapterPosition) {
        AdapterPosition = adapterPosition;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public void setDriverMobile(String driverMobile) {
        DriverMobile = driverMobile;
    }

    public void setDriverId(int driverId) {
        DriverId = driverId;
    }

    public CurrentJourneyDetailModel getCurrentJourneyDetail() {
        return currentJourneyDetail;
    }

    public void setCurrentJourneyDetail(CurrentJourneyDetailModel currentJourneyDetail) {
        this.currentJourneyDetail = currentJourneyDetail;
    }

    private CurrentJourneyDetailModel currentJourneyDetail;
}