package com.jeannypr.scientificstudy.leave.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestedLeaveDaysModel implements Parcelable {
    @SerializedName("date")
    @Expose
    public String Date;

    @SerializedName("dayType")
    @Expose
    public String DayType;
    public boolean IsHoliday;
    public String HolidayTitle;

    public String FormattedDate;
    public double TotalRequestedDays;

    public RequestedLeaveDaysModel(String date, String dayType, String formattedDate, Double totalRequestedDays, boolean isHoliday, String holidayTitle) {
        Date = date;
        DayType = dayType;
        FormattedDate = formattedDate;
        TotalRequestedDays = totalRequestedDays;
        IsHoliday = isHoliday;
        HolidayTitle = holidayTitle;
    }

    protected RequestedLeaveDaysModel(Parcel in) {
        Date = in.readString();
        DayType = in.readString();
        FormattedDate = in.readString();
        TotalRequestedDays = in.readDouble();
        IsHoliday = in.readByte() != 0;
        HolidayTitle = in.readString();
    }

    public static final Creator<RequestedLeaveDaysModel> CREATOR = new Creator<RequestedLeaveDaysModel>() {
        @Override
        public RequestedLeaveDaysModel createFromParcel(Parcel in) {
            return new RequestedLeaveDaysModel(in);
        }

        @Override
        public RequestedLeaveDaysModel[] newArray(int size) {
            return new RequestedLeaveDaysModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(DayType);
        dest.writeString(FormattedDate);
        dest.writeDouble(TotalRequestedDays);
        dest.writeByte((byte) (IsHoliday ? 1 : 0));
        dest.writeString(HolidayTitle);
    }
}
