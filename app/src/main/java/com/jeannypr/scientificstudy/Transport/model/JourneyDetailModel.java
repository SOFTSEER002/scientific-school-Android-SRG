package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JourneyDetailModel {
    @SerializedName("startTime")
    @Expose
    private String StartTime;

    @SerializedName("startDate")
    @Expose
    private String StartDate;

    @SerializedName("endTime")
    @Expose
    private String EndTime;

    @SerializedName("endDate")
    @Expose
    private String EndDate;

    @SerializedName("id")
    @Expose
    private int JourneyId;
    private String RouteName;

    @SerializedName("routeId")
    @Expose
    private int RouteId;
    private String VehicleNumber;

    @SerializedName("vehicleId")
    @Expose
    private int VehicleId;

    private String VehicleType;
    private String VehicleModel;
    private int SchoolId;
    private int UserId;
    private Boolean isPickup;

    @SerializedName("mode")
    @Expose
    private String Mode;


    public String getStartTime() {
        return StartTime;
    }

    public int getJourneyId() {
        return JourneyId;
    }


    public String getRouteName() {
        return RouteName == null ? "" : RouteName;
    }

    public int getRouteId() {
        return RouteId == -1 ? 0 : RouteId;
    }

    public String getVehicleNumber() {
        return VehicleNumber == null ? "" : VehicleNumber;
    }

    public int getVehicleId() {
        return VehicleId == -1 ? 0 : VehicleId;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setJourneyId(int journeyId) {
        JourneyId = journeyId;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleType() {
        return VehicleType == null ? "" : VehicleType;
    }

    public String getVehicleModel() {
        return VehicleModel == null ? "" : VehicleModel;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public int getUserId() {
        return UserId == -1 ? 0 : UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getMode() {
        return Mode == null ? "" : Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getStartDate() {
        return StartDate == null ? "" : StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndTime() {
        return EndTime == null ? "" : EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getEndDate() {
        return EndDate == null ? "" : EndTime;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Boolean getPickup() {
        return isPickup == null ? false : isPickup;
    }

    public void setPickup(Boolean pickup) {
        isPickup = pickup;
    }
}
