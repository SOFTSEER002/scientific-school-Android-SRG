package com.jeannypr.scientificstudy.Classwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityDetailModel implements Parcelable {
    @SerializedName("classActivity")
    @Expose
    public ActivityInfoModel activityModel;

    @SerializedName("classActivityItems")
    @Expose
    public List<ActivityItemModel> activityItemModel;

    public ActivityDetailModel(Parcel in) {
        activityModel = in.readParcelable(ActivityInfoModel.class.getClassLoader());
        activityItemModel = in.createTypedArrayList(ActivityItemModel.CREATOR);
    }

    public static final Creator<ActivityDetailModel> CREATOR = new Creator<ActivityDetailModel>() {
        @Override
        public ActivityDetailModel createFromParcel(Parcel in) {
            return new ActivityDetailModel(in);
        }

        @Override
        public ActivityDetailModel[] newArray(int size) {
            return new ActivityDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(activityModel, i);
        parcel.writeTypedList(activityItemModel);
    }
}
