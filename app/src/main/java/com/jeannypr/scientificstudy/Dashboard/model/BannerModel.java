package com.jeannypr.scientificstudy.Dashboard.model;

public class BannerModel {
    public String getBannername() {
        return bannername;
    }

    public String getHeading1() {
        return heading1;
    }

    public String getHeading2() {
        return heading2;
    }

    public String getHeading3() {
        return heading3;
    }

    public int getIcon() {
        return icon;
    }

    public String bannername;
    public String heading1;
    public String heading2;
    public String heading3;
    public int icon;


    public BannerModel(String bannername, String heading1, String heading2, String heading3, int icon) {
        this.bannername = bannername;
        this.heading1 = heading1;
        this.heading2 = heading2;
        this.heading3 = heading3;
        this.icon = icon;
    }
}
