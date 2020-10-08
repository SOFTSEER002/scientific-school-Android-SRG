package com.jeannypr.scientificstudy.Timetable.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeriodModel implements Parcelable {
    @SerializedName("startTime")
    @Expose
    public String StartTime;

    @SerializedName("endTime")
    @Expose
    public String EndTime;

    @SerializedName("classId")
    @Expose
    public int ClassId;

    @SerializedName("className")
    @Expose
    public String ClassName;

    public String getSubjectName() {
        return SubjectName != null ? SubjectName : "";
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    @SerializedName("subjectName")
    @Expose
    public String SubjectName;

    public String getTeacherName() {
        return TeacherName != null ? TeacherName : "";
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    @SerializedName("teacherName")
    @Expose
    public String TeacherName;

    @SerializedName("subjectId")
    @Expose
    public int SubjectId;

    @SerializedName("teacherUserId")
    @Expose
    public int TeacherUserId;


    @Override
    public int describeContents() {
        return 0;
    }

    public PeriodModel(){}

    public PeriodModel(Parcel parcel){
        StartTime = parcel.readString();
        EndTime = parcel.readString();
        ClassId = parcel.readInt();
        ClassName = parcel.readString();
        SubjectName = parcel.readString();
        TeacherName = parcel.readString();
        SubjectId = parcel.readInt();
        TeacherUserId = parcel.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(StartTime);
        dest.writeString(EndTime);
        dest.writeInt(ClassId);
        dest.writeString(ClassName);
        dest.writeString(SubjectName);
        dest.writeString(TeacherName);
        dest.writeInt(SubjectId);
        dest.writeInt(TeacherUserId);
    }

    public static final Parcelable.Creator<PeriodModel> CREATOR = new Parcelable.Creator<PeriodModel>() {
        public PeriodModel createFromParcel(Parcel in) {
            return new PeriodModel(in);
        }

        @Override
        public PeriodModel[] newArray(int size) {
            return new PeriodModel[size];
        }
    };

}
