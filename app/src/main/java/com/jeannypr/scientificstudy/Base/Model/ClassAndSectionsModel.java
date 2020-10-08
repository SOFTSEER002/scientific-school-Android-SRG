package com.jeannypr.scientificstudy.Base.Model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassAndSectionsModel extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer Id;


    @SerializedName("className")
    @Expose
    public String ClassName;


    @SerializedName("sections")
    @Expose
    public List<ClassAndSectionsModel> Sections;

    @SerializedName("sectionId")
    @Expose
    public Integer SectionId;


    @SerializedName("sectionName")
    @Expose
    public String SectionName;

    @Override
    public int describeContents() {
        return 0;
    }


    public ClassAndSectionsModel() {
    }

    public ClassAndSectionsModel(Parcel parcel) {
        SectionId = parcel.readInt();
        SectionName = parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SectionId);
        dest.writeString(SectionName);
    }

    public static final Parcelable.Creator<ClassAndSectionsModel> CREATOR = new Parcelable.Creator<ClassAndSectionsModel>() {
        public ClassAndSectionsModel createFromParcel(Parcel in) {
            return new ClassAndSectionsModel(in);
        }

        @Override
        public ClassAndSectionsModel[] newArray(int size) {
            return new ClassAndSectionsModel[size];
        }
    };
}
