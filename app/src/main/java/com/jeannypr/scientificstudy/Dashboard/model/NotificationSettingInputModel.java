package com.jeannypr.scientificstudy.Dashboard.model;

public class NotificationSettingInputModel {
    private String Module;
    private int schoolid;
    private int NotifyOnCompleteJourney;

    public String getModule() {
        return Module;
    }

    public int getSchoolid() {
        return schoolid;
    }

    public int getNotifyOnCompleteJourney() {
        return NotifyOnCompleteJourney;
    }

    public int getNotifyOnStartJourney() {
        return NotifyOnStartJourney;
    }

    private int NotifyOnStartJourney;

    public void setModule(String module) {
        Module = module;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }

    public void setNotifyOnCompleteJourney(int notifyOnCompleteJourney) {
        NotifyOnCompleteJourney = notifyOnCompleteJourney;
    }

    public void setNotifyOnStartJourney(int notifyOnStartJourney) {
        NotifyOnStartJourney = notifyOnStartJourney;
    }
}
