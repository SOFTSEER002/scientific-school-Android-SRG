package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransportModel {


    public String getRouteName() {
        return RouteName == null ? "" : RouteName;
    }

    public String getPlaceName() {
        return PlaceName == null ? "" : PlaceName;
    }

    public String getVehicleNo() {
        return VehicleNo == null ? "" : VehicleNo;
    }

    public String getVehicleType() {
        return VehicleType == null ? "" : VehicleType;
    }

    public String getDriverName() {
        return DriverName == null ? "" : DriverName;
    }

    public String getDriverMobile1() {
        return DriverMobile1 == null ? "" : DriverMobile1;
    }

    public String getDriverMobile2() {
        return DriverMobile2 == null ? "" : DriverMobile2;
    }

    public String getConductorName() {
        return ConductorName == null ? "" : ConductorName;
    }

    public String getConductorMobile1() {
        return ConductorMobile1 == null ? "" : ConductorMobile1;
    }

    public String getConductorMobile2() {
        return ConductorMobile2 == null ? "" : ConductorMobile2;
    }

    public String getStartTime() {
        return StartTime == null ? "" : StartTime;
    }

    public String getEndTime() {
        return EndTime == null ? "" : EndTime;
    }

    public String getTransportTypeText() {
        return TransportTypeText == null ? "" : TransportTypeText;
    }

    public String getCoordinator() {
        return Coordinator == null ? "" : Coordinator;
    }

    public String getCoordinatorMobile() {
        return CoordinatorMobile == null ? "" : CoordinatorMobile;
    }

//    public List<EmergencyContactsModel> getEmergencyContacts() {
//        return emergencyContacts;
//    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public void setDriverMobile1(String driverMobile1) {
        DriverMobile1 = driverMobile1;
    }

    public void setDriverMobile2(String driverMobile2) {
        DriverMobile2 = driverMobile2;
    }

    public void setConductorName(String conductorName) {
        ConductorName = conductorName;
    }

    public void setConductorMobile1(String conductorMobile1) {
        ConductorMobile1 = conductorMobile1;
    }

    public void setConductorMobile2(String conductorMobile2) {
        ConductorMobile2 = conductorMobile2;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public void setTransportTypeText(String transportTypeText) {
        TransportTypeText = transportTypeText;
    }

    public void setCoordinator(String coordinator) {
        Coordinator = coordinator;
    }

    public void setCoordinatorMobile(String coordinatorMobile) {
        CoordinatorMobile = coordinatorMobile;
    }


    @SerializedName("routeName")
    @Expose
    public String RouteName;

    @SerializedName("placeName")
    @Expose
    public String PlaceName;

    @SerializedName("vehicleNo")
    @Expose
    public String VehicleNo;

    @SerializedName("vehicleType")
    @Expose
    public String VehicleType;

    @SerializedName("driverName")
    @Expose
    public String DriverName;

    @SerializedName("driverMobile1")
    @Expose
    public String DriverMobile1;

    @SerializedName("driverMobile2")
    @Expose
    public String DriverMobile2;

    @SerializedName("conductorName")
    @Expose
    public String ConductorName;

    @SerializedName("conductorMobile1")
    @Expose
    public String ConductorMobile1;

    @SerializedName("conductorMobile2")
    @Expose
    public String ConductorMobile2;

    @SerializedName("startTime")
    @Expose
    public String StartTime;

    @SerializedName("endTime")
    @Expose
    public String EndTime;

    @SerializedName("transportTypeText")
    @Expose
    public String TransportTypeText;

    @SerializedName("coordinator")
    @Expose
    public String Coordinator;

    @SerializedName("coordinatorMobile")
    @Expose
    public String CoordinatorMobile;

    @SerializedName("emergencyContacts")
    @Expose
    public List<EmergencyContactsModel> EmergencyContacts;

    @SerializedName("isTransportAvailed")
    @Expose
    public Boolean IsTransportAvailed;
}
