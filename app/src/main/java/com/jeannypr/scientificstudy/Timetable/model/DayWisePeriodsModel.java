package com.jeannypr.scientificstudy.Timetable.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Teacher.model.TeacherProfileModel;

import java.io.Serializable;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class DayWisePeriodsModel implements Parcelable {
    @SerializedName("day")
    @Expose
    public String Day;

    @SerializedName("periods")
    @Expose
    public List<PeriodModel> Periods;

    @Override
    public int describeContents() {
        return 0;
    }

    public DayWisePeriodsModel(){}

    public DayWisePeriodsModel(Parcel parcel){
        Day = parcel.readString();
        parcel.readList(Periods,null);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Day);
        dest.writeList(Periods);
    }

    public static final Parcelable.Creator<DayWisePeriodsModel> CREATOR = new Parcelable.Creator<DayWisePeriodsModel>() {
        public DayWisePeriodsModel createFromParcel(Parcel in) {
            return new DayWisePeriodsModel(in);
        }

        @Override
        public DayWisePeriodsModel[] newArray(int size) {
            return new DayWisePeriodsModel[size];
        }
    };
}
