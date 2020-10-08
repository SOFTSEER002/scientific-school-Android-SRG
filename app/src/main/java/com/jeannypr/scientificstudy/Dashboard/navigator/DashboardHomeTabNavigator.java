package com.jeannypr.scientificstudy.Dashboard.navigator;

public interface DashboardHomeTabNavigator {

    /**
     * Override to set reminder.
     *  @param eventId
     * @param eventEndDate
     * @param eventType
     */
    void setReminder(int eventId, String eventEndDate, String eventType);

    /**
     * Override to save check in status.
     *
     * @param eventId
     * @param childAdapterPosition
     * @param parentAdapterPosition
     */
    void checkIn(int eventId, int childAdapterPosition, int parentAdapterPosition);

    /**
     * Override to save rsvp status.
     * @param eventId
     * @param rsvp
     */
    void rsvp(int eventId, String rsvp);

    /**
     * Override to show full desc.
     *
     * @param desc
     * @param title
     */
    void showFullDesc(String desc, String title);

    /**
     * Override to show full desc.
     *
     * @param desc
     * @param title
     * @param startDate
     */
    void showFullDesc(String desc, String title, String startDate);

    /**
     * Override to navigate to web browser.
     * @param url
     * @param title
     * @param subtitle
     */
    void openBrowserInApp(String url, String title, String subtitle, int errorMsg);

    /**
     * Override to navigate to web browser.
     * @param url
     */
    void openLinkInSystemBrowser(String url, int errorMsg);
}
