package com.jeannypr.scientificstudy.Student.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.Bean;

import java.util.List;

public class AttendanceBean extends Bean {

    @SerializedName("data")
    @Expose
    public AttendanceModel data;


    public class AttendanceModel{
        @SerializedName("attendance")
        @Expose
        public List<StudentAttendnaceModel> Attendances;

        @SerializedName("holidays")
        @Expose
        public List<HolidayModel> Holidays;
    }

}
