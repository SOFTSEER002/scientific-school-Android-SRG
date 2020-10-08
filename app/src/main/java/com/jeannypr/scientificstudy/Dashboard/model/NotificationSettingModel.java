package com.jeannypr.scientificstudy.Dashboard.model;

public class NotificationSettingModel {
    private NotificationSettingModel notifications;

    public NotificationSettingModel getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationSettingModel notifications) {
        this.notifications = notifications;
    }

    public Boolean getNotifyOnStartJourney() {
        return notifyOnStartJourney == null ? false : notifyOnStartJourney;
    }

    public void setNotifyOnStartJourney(Boolean notifyOnStartJourney) {
        this.notifyOnStartJourney = notifyOnStartJourney;
    }

    public Boolean getNotifyOnCompleteJourney() {
        return notifyOnCompleteJourney == null ? false : notifyOnCompleteJourney;
    }

    public void setNotifyOnCompleteJourney(Boolean notifyOnCompleteJourney) {
        this.notifyOnCompleteJourney = notifyOnCompleteJourney;
    }

    private Boolean notifyOnStartJourney;
    private Boolean notifyOnCompleteJourney;
}
