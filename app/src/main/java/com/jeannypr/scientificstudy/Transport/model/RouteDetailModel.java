package com.jeannypr.scientificstudy.Transport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteDetailModel {
    @SerializedName("routeId")
    @Expose
    public int RouteId;

    @SerializedName("studentName")
    @Expose
    public String StudentName;

    @SerializedName("admissionNumber")
    @Expose
    public String AdmissionNumber;

    @SerializedName("className")
    @Expose
    public String ClassName;

    @SerializedName("routeName")
    @Expose
    public String RouteName;

    @SerializedName("stoppageName")
    @Expose
    public String StoppageName;

    @SerializedName("parentName")
    @Expose
    public String ParentName;

    @SerializedName("parentPhone")
    @Expose
    public String ParentPhone;

    @SerializedName("vehicleNo")
    @Expose
    public String VehicleNo;

    @SerializedName("transportFee")
    @Expose
    public String TransportFee;

    @SerializedName("rollNumber")
    @Expose
    public String RollNumber;

    public String getFatherMobile() {
        return FatherMobile == null ? "" : FatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        FatherMobile = fatherMobile;
    }

    @SerializedName("fatherMobile")
    @Expose
    private String FatherMobile;

    @SerializedName("areaId")
    @Expose
    public int AreaId;

    @SerializedName("classDisplayOrder")
    @Expose
    public String ClassDisplayOrder;
}
