package com.jeannypr.scientificstudy.Transport.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverModel extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    private int Id;

    @Bindable
    @SerializedName("firstName")
    @Expose
    private String FirstName;

    @Bindable
    @SerializedName("lastName")
    @Expose
    private String LastName;

    @SerializedName("dob")
    @Expose
    private String Dob;

    @SerializedName("address")
    @Expose
    private String Address;

    @SerializedName("mobile1")
    @Expose
    private String Mobile1;

    @SerializedName("mobile2")
    @Expose
    private String Mobile2;

    @SerializedName("schoolId")
    @Expose
    private int SchoolId;

    @SerializedName("type")
    @Expose
    private String Type;

    @SerializedName("drivinglicenceNo")
    @Expose
    private String DrivingLicenceNo;

    @SerializedName("proofType")
    @Expose
    private String ProofType;

    @SerializedName("proofNo")
    @Expose
    private String ProofNo;

    @SerializedName("userId")
    @Expose
    private int UserId;

    @SerializedName("userName")
    @Expose
    private String UserName;

    @SerializedName("userPassword")
    @Expose
    private String UserPassword;

    public void setId(int id) {
        Id = id;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setMobile1(String mobile1) {
        Mobile1 = mobile1;
    }

    public void setMobile2(String mobile2) {
        Mobile2 = mobile2;
    }

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setDrivingLicenceNo(String drivingLicenceNo) {
        DrivingLicenceNo = drivingLicenceNo;
    }

    public void setProofType(String proofType) {
        ProofType = proofType;
    }

    public void setProofNo(String proofNo) {
        ProofNo = proofNo;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public int getId() {
        return Id == -1 ? 0 : Id;
    }

    public String getFirstName() {
        return FirstName == null ? "" : FirstName;
    }

    public String getLastName() {
        return LastName == null ? "" : LastName;
    }

    public String getDob() {
        return Dob == null ? "" : Dob;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public String getMobile1() {
        return Mobile1 == null ? "" : Mobile1;
    }

    public String getMobile2() {
        return Mobile2 == null ? "" : Mobile2;
    }

    public int getSchoolId() {
        return SchoolId == -1 ? 0 : SchoolId;
    }

    public String getType() {
        return Type == null ? "" : Type;
    }

    public String getDrivingLicenceNo() {
        return DrivingLicenceNo == null ? "" : DrivingLicenceNo;
    }

    public String getProofType() {
        return ProofType == null ? "" : ProofType;
    }

    public String getProofNo() {
        return ProofNo == null ? "" : ProofNo;
    }

    public int getUserId() {
        return UserId == -1 ? 0 : UserId;
    }

    public String getUserName() {
        return UserName == null ? "" : UserName;
    }

    public String getUserPassword() {
        return UserPassword == null ? "" : UserPassword;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public DriverModel() {
    }

    private DriverModel(Parcel parcel) {
        setId(parcel.readInt());
        setFirstName(parcel.readString());
        setLastName(parcel.readString());
        setDob(parcel.readString());
        setAddress(parcel.readString());
        setMobile1(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(getDob());
        dest.writeString(getAddress());
        dest.writeString(getMobile1());

    }

    public static final Parcelable.Creator<DriverModel> CREATOR = new Parcelable.Creator<DriverModel>() {
        public DriverModel createFromParcel(Parcel in) {
            return new DriverModel(in);
        }

        @Override
        public DriverModel[] newArray(int size) {
            return new DriverModel[size];
        }
    };
}
