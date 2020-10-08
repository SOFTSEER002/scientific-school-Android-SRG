package com.jeannypr.scientificstudy.Classwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityItemModel implements Parcelable {
    @SerializedName("fileType")
    @Expose
    public int FileType;

    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("attachmentName")
    @Expose
    public String AttachmentName;

    @SerializedName("path")
    @Expose
    public String Path;

    @SerializedName("fileExtension")
    @Expose
    public String FileExtension; //not available

    @SerializedName("fileSize")
    @Expose
    public String Size;

    @SerializedName("thumbnailPath")
    @Expose
    public String ThumbnailPath; //not available

    @SerializedName("hasThumbnail")
    @Expose
    public Boolean HasThumbnail; //not available

    @SerializedName("id")
    @Expose
    public int Id; //not available

    private ActivityItemModel(Parcel in) {
        FileType = in.readInt();
        Title = in.readString();
        AttachmentName = in.readString();
        Path = in.readString();
        FileExtension = in.readString();
        Size = in.readString();
        ThumbnailPath = in.readString();
        byte tmpHasThumbnail = in.readByte();
        HasThumbnail = tmpHasThumbnail == 0 ? null : tmpHasThumbnail == 1;
        Id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(FileType);
        dest.writeString(Title);
        dest.writeString(AttachmentName);
        dest.writeString(Path);
        dest.writeString(FileExtension);
        dest.writeString(Size);
        dest.writeString(ThumbnailPath);
        dest.writeByte((byte) (HasThumbnail == null ? 0 : HasThumbnail ? 1 : 2));
        dest.writeInt(Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityItemModel> CREATOR = new Creator<ActivityItemModel>() {
        @Override
        public ActivityItemModel createFromParcel(Parcel in) {
            return new ActivityItemModel(in);
        }

        @Override
        public ActivityItemModel[] newArray(int size) {
            return new ActivityItemModel[size];
        }
    };
}
