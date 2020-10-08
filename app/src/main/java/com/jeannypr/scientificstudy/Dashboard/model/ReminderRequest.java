package com.jeannypr.scientificstudy.Dashboard.model;

public class ReminderRequest {
    private String task;
    private int eventId;
    private String date;

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public void setAreYouInterested(int rsvp) {
        this.interestStatus = rsvp;
    }

    private int interestStatus;

    public void setTask(String task) {
        this.task = task;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;
}
