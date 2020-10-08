package com.jeannypr.scientificstudy.Login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kannuk on
 **/
class AdminDetailModel implements Parcelable {
    @SerializedName("Mobile")
    @Expose
    private String Mobile;

    @SerializedName("email")
    @Expose
    private String email;

    private AdminDetailModel(Parcel in) {
        Mobile = in.readString();
        email = in.readString();
    }

    public static final Creator<AdminDetailModel> CREATOR = new Creator<AdminDetailModel>() {
        @Override
        public AdminDetailModel createFromParcel(Parcel in) {
            return new AdminDetailModel(in);
        }

        @Override
        public AdminDetailModel[] newArray(int size) {
            return new AdminDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Mobile);
        parcel.writeString(email);
    }
}
