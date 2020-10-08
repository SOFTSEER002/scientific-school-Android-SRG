package com.jeannypr.scientificstudy.Classwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;

import java.util.List;

public class ActivityInfoModel implements Parcelable {
    @SerializedName("id")
    @Expose
    public int Id;

    @SerializedName("activityType")
    @Expose
    public int ActivityType;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("activityDate")
    @Expose
    public String ActivityDate;

    @SerializedName("activitySubmissionDate")
    @Expose
    public String ActivitySubmissionDate;

    @SerializedName("clases")
    @Expose
    public List<ClassModel> Classes;

    @SerializedName("classId")
    @Expose
    public int ClassId;

    @SerializedName("name")
    @Expose
    public String ClassName;

    @SerializedName("subjectName")
    @Expose
    public String SubjectName;

    @SerializedName("subjectId")
    @Expose
    public int SubjectId;

    @SerializedName("subjectTeacher")
    @Expose
    public String TeacherName;

    @SerializedName("isAssignedToClass")
    @Expose
    public Boolean IsAssignedToClass;

    protected ActivityInfoModel(Parcel in) {
        Id = in.readInt();
        ActivityType = in.readInt();
        Title = in.readString();
        ActivityDate = in.readString();
        ActivitySubmissionDate = in.readString();
        Classes = in.createTypedArrayList(ClassModel.CREATOR);
        ClassId = in.readInt();
        ClassName = in.readString();
        SubjectName = in.readString();
        SubjectId = in.readInt();
        TeacherName = in.readString();
        byte tmpIsAssignedToClass = in.readByte();
        IsAssignedToClass = tmpIsAssignedToClass == 0 ? null : tmpIsAssignedToClass == 1;
    }

    public static final Creator<ActivityInfoModel> CREATOR = new Creator<ActivityInfoModel>() {
        @Override
        public ActivityInfoModel createFromParcel(Parcel in) {
            return new ActivityInfoModel(in);
        }

        @Override
        public ActivityInfoModel[] newArray(int size) {
            return new ActivityInfoModel[size];
        }
    };

    public String getIsAssignedToClass() {
        return Boolean.toString(IsAssignedToClass == null ? false : IsAssignedToClass);
    }

    public String getActivityType() {
        return String.valueOf(ActivityType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(ActivityType);
        parcel.writeString(Title);
        parcel.writeString(ActivityDate);
        parcel.writeString(ActivitySubmissionDate);
        parcel.writeTypedList(Classes);
        parcel.writeInt(ClassId);
        parcel.writeString(ClassName);
        parcel.writeString(SubjectName);
        parcel.writeInt(SubjectId);
        parcel.writeString(TeacherName);
        parcel.writeByte((byte) (IsAssignedToClass == null ? 0 : IsAssignedToClass ? 1 : 2));
    }
}
