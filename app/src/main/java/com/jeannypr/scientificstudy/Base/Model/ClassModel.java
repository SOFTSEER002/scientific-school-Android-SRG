package com.jeannypr.scientificstudy.Base.Model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassModel extends BaseObservable implements Parcelable {

    @SerializedName("classId")
    @Expose
    public Integer Id;

    @Bindable
    @SerializedName("name")
    @Expose
    public String Name;


    @SerializedName("totalStudent")
    @Expose
    private int TotalNoStudents;

    @SerializedName("classMonitor")
    @Expose
    private String ClassMonitor;

    public String getClassMonitor() {
        return ClassMonitor == null ? "" : ClassMonitor;
    }

    @Bindable
    public int getTotalNoStudents() {
        return TotalNoStudents == -1 ? 0 : TotalNoStudents;
    }


    public String getClassTeacher() {
        return ClassTeacher == null ? "" : ClassTeacher;
    }

    public void setClassTeacher(String classTeacher) {
        ClassTeacher = classTeacher;
    }

    @Bindable
    @SerializedName("teacher")
    @Expose
    private String ClassTeacher;

    @Bindable
    @SerializedName("teacherImage")
    @Expose
    public String ClassTeacherImage;

    public int adapterPosition;

    @Override
    public int describeContents() {
        return 0;
    }


    public ClassModel() {
    }

    public ClassModel(Parcel parcel) {
        Id = parcel.readInt();
        Name = parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
    }

    public static final Parcelable.Creator<ClassModel> CREATOR = new Parcelable.Creator<ClassModel>() {
        public ClassModel createFromParcel(Parcel in) {
            return new ClassModel(in);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };
}
