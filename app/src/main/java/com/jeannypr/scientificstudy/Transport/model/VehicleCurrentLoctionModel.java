package com.jeannypr.scientificstudy.Transport.model;

import androidx.room.Query;
import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleCurrentLoctionModel extends BaseObservable {

    @SerializedName("latitude")
    @Expose
    private String Latitude;

    @SerializedName("longitude")
    @Expose
    private String Longitude;

    @SerializedName("journeyStatus")
    @Expose
    private int Status;

    public int getAttendance() {
        return Attendance == -1 ? 0 : Attendance;
    }

    public void setAttendance(int attendance) {
        Attendance = attendance;
    }

    public Boolean isPickup() {
        return IsPickup == null ? false : IsPickup;
    }

    public void setPickup(Boolean pickup) {
        IsPickup = pickup;
    }

    @SerializedName("attendance")
    @Expose
    private int Attendance;

    @SerializedName("isPickUp")
    @Expose
    private Boolean IsPickup;

    public String getLatitude() {
        return Latitude == null ? "" : Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude == null ? "" : Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getStatus() {
        return Status == -1 ? 0 : Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
