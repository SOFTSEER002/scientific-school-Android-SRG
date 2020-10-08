package com.jeannypr.scientificstudy.Login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kannuk on
 **/
public class ForgetPasswordModel implements Parcelable {
    @SerializedName("ParentPhone")
    @Expose
    private String ParentPhone;

    @SerializedName("parentEmail")
    @Expose
    private String parentEmail;

    @SerializedName("isPhoneExists")
    @Expose
    private Boolean isPhoneExists;

    @SerializedName("isEmailExists")
    @Expose
    private Boolean isEmailExists;

    @SerializedName("admindetail")
    @Expose
    private AdminDetailModel adminDetail;

    public String admNo;


    private ForgetPasswordModel(Parcel in) {
        ParentPhone = in.readString();
        parentEmail = in.readString();
        byte tmpIsPhoneExists = in.readByte();
        isPhoneExists = tmpIsPhoneExists == 0 ? null : tmpIsPhoneExists == 1;
        byte tmpIsEmailExists = in.readByte();
        isEmailExists = tmpIsEmailExists == 0 ? null : tmpIsEmailExists == 1;
        adminDetail = in.readParcelable(AdminDetailModel.class.getClassLoader());
        admNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ParentPhone);
        parcel.writeString(parentEmail);
        parcel.writeByte((byte) (isPhoneExists == null ? 0 : isPhoneExists ? 1 : 2));
        parcel.writeByte((byte) (isEmailExists == null ? 0 : isEmailExists ? 1 : 2));
        parcel.writeParcelable(adminDetail, i);
        parcel.writeString(admNo);
    }

    public static final Creator<ForgetPasswordModel> CREATOR = new Creator<ForgetPasswordModel>() {
        @Override
        public ForgetPasswordModel createFromParcel(Parcel in) {
            return new ForgetPasswordModel(in);
        }

        @Override
        public ForgetPasswordModel[] newArray(int size) {
            return new ForgetPasswordModel[size];
        }
    };

    public String getParentPhone() {
        return ParentPhone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public Boolean getPhoneExists() {
        return isPhoneExists;
    }

    public Boolean getEmailExists() {
        return isEmailExists;
    }

    public AdminDetailModel getAdmindetail() {
        return adminDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
