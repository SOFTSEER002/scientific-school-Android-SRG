package com.jeannypr.scientificstudy.Transport.model;

public class LocationDetailModel {

    private int SchoolId;
    private int JourneyId;
    private String Latitude;
    private String Longitude;

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setJourneyId(int journeyId) {
        JourneyId = journeyId;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
