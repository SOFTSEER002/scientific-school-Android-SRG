package com.jeannypr.scientificstudy.Transport.model;

public class LoctionDetailModel {

    private int SchoolId;
    private int JourneyId;
    private Double Latitude;
    private Double Longitude;

    public void setSchoolId(int schoolId) {
        SchoolId = schoolId;
    }

    public void setJourneyId(int journeyId) {
        JourneyId = journeyId;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
