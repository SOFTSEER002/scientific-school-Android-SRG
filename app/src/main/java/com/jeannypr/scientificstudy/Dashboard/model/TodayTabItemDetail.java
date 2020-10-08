package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayTabItemDetail {
    @SerializedName("id")
    @Expose
    private int ID;

    @SerializedName("title")
    @Expose
    private String Title;

    @SerializedName("subTitle")
    @Expose
    private String Subtitle;

    @SerializedName("description")
    @Expose
    private String Description;

    @SerializedName("startDate")
    @Expose
    private String StratDate;

    @SerializedName("endDate")
    @Expose
    private String EndDate;

    @SerializedName("venue")
    @Expose
    private String Venue;

    @SerializedName("imagePath")
    @Expose
    private String ImagePath;

   /* @SerializedName("priority")
    @Expose
    public int priority;*/

    @SerializedName("extraKey")
    @Expose
    private TodayTabExtraKeysModel ExtraKeys;

    public int adapterPosition;

    public int getID() {
        return ID == -1 ? 0 : ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title == null ? "" : Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubtitle() {
        return Subtitle == null ? "" : Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public String getDescription() {
        return Description == null ? "" : Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStratDate() {
        return StratDate == null ? "" : StratDate;
    }

    public void setStratDate(String stratDate) {
        StratDate = stratDate;
    }

    public String getEndDate() {
        return EndDate == null ? "" : EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getVenue() {
        return Venue == null ? "" : Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getImagePath() {
        return ImagePath == null ? "" : ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public TodayTabExtraKeysModel getExtraKeys() {
        return ExtraKeys == null ? new TodayTabExtraKeysModel() : ExtraKeys;
    }

    public void setExtraKeys(TodayTabExtraKeysModel extraKeys) {
        ExtraKeys = extraKeys;
    }
}
