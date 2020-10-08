package com.jeannypr.scientificstudy.Student.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.anychart.editor.Step;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Inventory.model.PurchaseSummaryItemsModel;
import com.jeannypr.scientificstudy.Timetable.model.DayWisePeriodsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentProfileModel implements Parcelable {
    @SerializedName("personalDetails")
    @Expose
    public StudentProfileModel PersonalDetails;
    public int SubjectAdapterPosition;

    @SerializedName("physicalDetail")
    @Expose
    public StudentProfileModel PhysicalDetail;

    @SerializedName("otherDetails")
    @Expose
    public StudentProfileModel OtherDetails;

    @SerializedName("subjectAndTeacher")
    @Expose
    public ArrayList<StudentProfileModel> SubjectAndTeacher;

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @SerializedName("name")
    @Expose
    private String Name;

    public String getDateOfBirth() {
        return DateOfBirth == null ? "" : DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return Sex == null ? "" : Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @SerializedName("dob")
    @Expose
    private String DateOfBirth;

    public String getAdmissionNo() {
        return AdmissionNo == null ? "" : AdmissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        AdmissionNo = admissionNo;
    }

    @SerializedName("admission")
    @Expose
    private String AdmissionNo;

    @SerializedName("sex")
    @Expose
    private String Sex;

    @SerializedName("mobile")
    @Expose
    public String MobileNo;

    @SerializedName("address")
    @Expose
    private String Address;

    @SerializedName("email")
    @Expose
    public String EmailId;

    @SerializedName("category")
    @Expose
    public String Category;

    @SerializedName("religion")
    @Expose
    public String Religion;

    @SerializedName("adharCardNumber")
    @Expose
    public String AdharCardNumber;

    @SerializedName("classname")
    @Expose
    public String Classname;

    @SerializedName("subjectName")
    @Expose
    private String SubjectName;

    public String getSubjectName() {
        return SubjectName == null ? "" : SubjectName.substring(0, 1).toUpperCase() + SubjectName.substring(1).toLowerCase();
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getSubjectTeacherName() {
        return SubjectTeacherName == null ? "" : SubjectTeacherName.substring(0, 1).toUpperCase() + SubjectTeacherName.substring(1).toLowerCase();
    }

    public void setSubjectTeacherName(String subjectTeacherName) {
        SubjectTeacherName = subjectTeacherName;
    }

    @SerializedName("subjectTeacherName")
    @Expose
    private String SubjectTeacherName;

    @SerializedName("bloodGroup")
    @Expose
    public String BloodGroup;

    @SerializedName("height")
    @Expose
    public String Height;

    @SerializedName("weight")
    @Expose
    public String Weight;

    @SerializedName("house")
    @Expose
    public String House;

    @SerializedName("isHosteller")
    @Expose
    public String IsHosteller;

    @SerializedName("isTransport")
    @Expose
    public String IsTransport;

    @SerializedName("rollNo")
    @Expose
    public String RollNo;

    @SerializedName("fatherImgPath")
    @Expose
    private String FatherImgPath;

    @SerializedName("motherImgPath")
    @Expose
    private String MotherImgPath;

    public String getMotherMobileNo() {
        return MotherMobileNo == null || MotherMobileNo.equals(Constants.DEFAULT_MOBILE) ? "" : MotherMobileNo;
    }

    public void setMotherMobileNo(String motherMobileNo) {
        MotherMobileNo = motherMobileNo;
    }

    public String getMotherEmail() {
        return MotherEmail == null ? "" : MotherEmail;
    }

    public void setMotherEmail(String motherEmail) {
        MotherEmail = motherEmail;
    }

    @SerializedName("motherMobileNo")
    @Expose
    private String MotherMobileNo;

    @SerializedName("motherEmail")
    @Expose
    private String MotherEmail;

    @SerializedName("fatherName")
    @Expose
    public String FatherName;

    public StudentProfileModel getPersonalDetails() {
        return PersonalDetails;
    }

    public String getRollNo() {
        return RollNo == null ? "" : RollNo;
    }

    public String getFatherImgPath() {
        return FatherImgPath == null ? "" : FatherImgPath;
    }

    public String getMotherImgPath() {
        return MotherImgPath == null ? "" : MotherImgPath;
    }

    public String getFatherName() {
        return FatherName == null ? "" : FatherName.substring(0, 1).toUpperCase() + FatherName.substring(1).toLowerCase();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public StudentProfileModel() {
    }

    public StudentProfileModel(Parcel parcel) {
        parcel.readList(SubjectAndTeacher, null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(SubjectAndTeacher);
    }

    public static final Parcelable.Creator<StudentProfileModel> CREATOR = new Parcelable.Creator<StudentProfileModel>() {
        public StudentProfileModel createFromParcel(Parcel in) {
            return new StudentProfileModel(in);
        }

        @Override
        public StudentProfileModel[] newArray(int size) {
            return new StudentProfileModel[size];
        }
    };

    public Boolean getCanSeeContactNumber() {
        return canSeeContactNumber == null ? false : canSeeContactNumber;
    }

    public void setCanSeeContactNumber(Boolean canSeeContactNumber) {
        this.canSeeContactNumber = canSeeContactNumber;
    }

    @SerializedName("canSeeContactNumber")
    @Expose
    private Boolean canSeeContactNumber;
}
