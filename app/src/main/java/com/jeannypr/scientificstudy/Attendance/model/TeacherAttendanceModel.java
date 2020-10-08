package com.jeannypr.scientificstudy.Attendance.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherAttendanceModel extends BaseObservable {
    @SerializedName("id")
    @Expose
    public int Id;

//    public String getName() {
//        return Name != null ? Name : "";
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }

    @Bindable
    @SerializedName("teacherName")
    @Expose
    public String Name;

    @Bindable
    @SerializedName("attendance")
    @Expose
    public Integer Attendance;

    @Bindable
    @SerializedName("notes")
    @Expose
    public String Notes;

    @Bindable
    @SerializedName("isOutDoor")
    @Expose
    public Boolean IsOutdoor;

    @Bindable
    @SerializedName("isExtra")
    @Expose
    public Boolean IsExtra;
    @SerializedName("branchId")
    @Expose
    public  int BranchId;

    @SerializedName("branchName")
    @Expose
    public String Branchname;

    @SerializedName("designation")
    @Expose
    public String Designation;

    @SerializedName("attendanceTime")
    @Expose
    public String AttendanceTime;

    @SerializedName("takenThrough")
    @Expose
    public String TakenThrough;



    public void onAttendanceClicked(int value){

        Attendance = value;

        if(Attendance == null || Attendance == 0){
            IsExtra = false;
            IsOutdoor = false;
            Notes = "";

        }


    }
}
