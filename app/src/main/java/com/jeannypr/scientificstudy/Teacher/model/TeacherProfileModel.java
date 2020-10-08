package com.jeannypr.scientificstudy.Teacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherProfileModel implements Parcelable {
    public int getId() {
        return Id > 0 ? Id : -1;
    }

    public String getName() {
        return Name != null ? Name : "";
    }

    public String getEmail() {
        return Email != null ? Email : "";
    }

    public String getMobile() {
        return Mobile != null ? Mobile : "";
    }

    public String getFatherName() {
        return FatherName != null ? FatherName : "";
    }

    public String getHusbandName() {
        return HusbandName != null ? HusbandName : "";
    }

    public String getBloodGroup() {
        return BloodGroup != null ? BloodGroup : "";
    }

    public String getState() {
        return State != null ? State : "";
    }

    public String getCity() {
        return City != null ? City : "";
    }

    public String getReligion() {
        return Religion != null ? Religion : "";
    }

    public String getCaste() {
        return Caste != null ? Caste : "";
    }

    public String getAddress() {
        return Address != null ? Address : "";
    }

    public String getExperience() {
        return Experience != null ? Experience : "";
    }

    public String getDesignation() {
        return Designation != null ? Designation : "";
    }

    public Boolean getTrained() {

        return IsTrained != null ? IsTrained : false;
    }

    public Boolean getTeaching() {
        return IsTeaching != null ? IsTeaching : false;
    }

    public String getJobType() {
        return JobType != null ? JobType : "";
    }

    public String getDepartment() {
        return Department != null ? Department : "";
    }

    public String getImagePath() {
        return ImagePath != null ? ImagePath : "";
    }

    public int getUserId() {
        return UserId > 0 ? UserId : 0;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public void setHusbandName(String husbandName) {
        HusbandName = husbandName;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public void setState(String state) {
        State = state;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public void setCaste(String caste) {
        Caste = caste;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public void setTrained(Boolean trained) {
        IsTrained = trained;
    }

    public void setTeaching(Boolean teaching) {
        IsTeaching = teaching;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("teacherName")
    @Expose
    public String Name;

    @SerializedName("email")
    @Expose
    public String Email;

    @SerializedName("mobile")
    @Expose
    public String Mobile;

    @SerializedName("fatherName")
    @Expose
    public String FatherName;

    @SerializedName("husbandName")
    @Expose
    public String HusbandName;

    @SerializedName("bloodGroup")
    @Expose
    public String BloodGroup;

    @SerializedName("state")
    @Expose
    public String State;

    @SerializedName("city")
    @Expose
    public String City;

    @SerializedName("religion")
    @Expose
    public String Religion;

    @SerializedName("caste")
    @Expose
    public String Caste;

    @SerializedName("address")
    @Expose
    public String Address;

    @SerializedName("experience")
    @Expose
    public String Experience;

    @SerializedName("designation")
    @Expose
    public String Designation;

    @SerializedName("isTrained")
    @Expose
    public Boolean IsTrained;

    @SerializedName("isTeaching")
    @Expose
    public Boolean IsTeaching;

    @SerializedName("jobType")
    @Expose
    public String JobType;

    @SerializedName("department")
    @Expose
    public String Department;


    @SerializedName("imagePath")
    @Expose
    public String ImagePath;

    @SerializedName("userId")
    @Expose
    public int UserId;

    public TeacherProfileModel() {
    }

    public TeacherProfileModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Email = in.readString();
        Mobile = in.readString();
        FatherName = in.readString();
        HusbandName = in.readString();
        BloodGroup = in.readString();
        State = in.readString();
        City = in.readString();
        Religion = in.readString();
        Caste = in.readString();
        Address = in.readString();
        Experience = in.readString();
        Designation = in.readString();
        IsTrained = in.readByte() != 0;
        IsTeaching = in.readByte() != 0;
        JobType = in.readString();
        Department = in.readString();
        ImagePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(Mobile);
        dest.writeString(FatherName);
        dest.writeString(HusbandName);
        dest.writeString(BloodGroup);
        dest.writeString(State);
        dest.writeString(City);
        dest.writeString(Religion);
        dest.writeString(Caste);
        dest.writeString(Address);
        dest.writeString(Experience);
        dest.writeString(Designation);
        dest.writeByte((byte) (IsTrained ? 1 : 0));
        dest.writeByte((byte) (IsTeaching ? 1 : 0));
        dest.writeString(JobType);
        dest.writeString(Department);
        dest.writeString(ImagePath);
    }

    public static final Parcelable.Creator<TeacherProfileModel> CREATOR = new Parcelable.Creator<TeacherProfileModel>() {
        public TeacherProfileModel createFromParcel(Parcel in) {
            return new TeacherProfileModel(in);
        }

        @Override
        public TeacherProfileModel[] newArray(int size) {
            return new TeacherProfileModel[size];
        }
    };
}
