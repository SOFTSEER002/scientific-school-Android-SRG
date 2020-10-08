package com.jeannypr.scientificstudy.Article.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleListDetailsModel implements Parcelable {

    @SerializedName("authorId")
    @Expose
    public int authorId;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("authorName")
    @Expose
    public String authorName;

    @SerializedName("authorOrganization")
    @Expose
    public String authorOrganization;

    @SerializedName("authorNumber")
    @Expose
    public String authorNumber;



    @SerializedName("authorEmail")
    @Expose
    public String authorEmail;


    @SerializedName("categoryName")
    @Expose
    public String categoryName;


    @SerializedName("createdDate")
    @Expose
    public String createdDate;

    @SerializedName("articleTitle")
    @Expose
    public String articleTitle;

    @SerializedName("articleDescription")
    @Expose
    public String articleDescription;


    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

    @SerializedName("videoUrl")
    @Expose
    public String videoUrl;

    @SerializedName("schoolCode")
    @Expose
    public String schoolCode;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.authorId);
        dest.writeInt(this.id);
        dest.writeString(this.authorName);
        dest.writeString(this.authorOrganization);
        dest.writeString(this.authorNumber);
        dest.writeString(this.authorEmail);
        dest.writeString(this.categoryName);
        dest.writeString(this.createdDate);
        dest.writeString(this.articleTitle);
        dest.writeString(this.articleDescription);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.schoolCode);
    }

    public ArticleListDetailsModel() {
    }

    protected ArticleListDetailsModel(Parcel in) {
        this.authorId = in.readInt();
        this.id = in.readInt();
        this.authorName = in.readString();
        this.authorOrganization = in.readString();
        this.authorNumber = in.readString();
        this.authorEmail = in.readString();
        this.categoryName = in.readString();
        this.createdDate = in.readString();
        this.articleTitle = in.readString();
        this.articleDescription = in.readString();
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.videoUrl = in.readString();
        this.schoolCode = in.readString();
    }

    public static final Creator<ArticleListDetailsModel> CREATOR = new Creator<ArticleListDetailsModel>() {
        @Override
        public ArticleListDetailsModel createFromParcel(Parcel source) {
            return new ArticleListDetailsModel(source);
        }

        @Override
        public ArticleListDetailsModel[] newArray(int size) {
            return new ArticleListDetailsModel[size];
        }
    };
}
