package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehiclesModel extends BaseObservable {
    public int getVehicleId() {
        return VehicleId == -1 ? 0 : VehicleId;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return VehicleNo == null ? "" : VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getModelName() {
        return ModelName == null ? "" : ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getVehicleType() {
        return VehicleType == null ? "" : VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    @Bindable
    @SerializedName("vehicleNo")
    @Expose
    private String VehicleNo;

    @Bindable
    @SerializedName("model")
    @Expose
    private String ModelName;

    @Bindable
    @SerializedName("vehicleType")
    @Expose
    private String VehicleType;

    @SerializedName("id")
    @Expose
    private int VehicleId;

    public boolean isChecked;
    public int AdapterPosition;
}
