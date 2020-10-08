package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentJourneyDetailModel extends BaseObservable {

    @Bindable
    @SerializedName("startTime")
    @Expose
    private String StartTime;

    @Bindable
    @SerializedName("endTime")
    @Expose
    private String EndTime;

    @Bindable
    @SerializedName("lastNotification")
    @Expose
    private String LastNotificationMessage;

    @Bindable
    @SerializedName("latitude")
    @Expose
    private String Lattitude;

    @Bindable
    @SerializedName("longitude")
    @Expose
    private String Longitude;

    public int getJourneyId() {
        return JourneyId ;
    }

    public Boolean isPickup() {
        return IsPickup == null ? true : IsPickup;
    }

    public String getStartTime() {
        return StartTime == null ? "" : StartTime;
    }

    public String getLastNotificationMessage() {
        return LastNotificationMessage == null ? "" : LastNotificationMessage;
    }

    public String getLattitude() {
        return Lattitude == null ? "" : Lattitude;
    }

    public String getLongitude() {
        return Longitude == null ? "" : Longitude;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public void setLastNotificationMessage(String lastNotificationMessage) {
        LastNotificationMessage = lastNotificationMessage;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Bindable
    @SerializedName("journeyId")
    @Expose
    private int JourneyId;

    @Bindable
    @SerializedName("isPickup")
    @Expose
    private Boolean IsPickup;

    public void setJourneyId(int journeyId) {
        JourneyId = journeyId;
    }

    public void setPickup(Boolean pickup) {
        IsPickup = pickup;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime == null ? "" : EndTime;
    }

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

    public String getDriverMobile() {
        return DriverMobile == null ? "" : DriverMobile;
    }

    public int getDriverId() {
        return DriverId == -1 ? 0 : DriverId;
    }
}
