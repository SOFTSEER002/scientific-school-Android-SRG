package com.jeannypr.scientificstudy.Dashboard.navigator;

public interface EventNavigator {

    /**
     * Override to save check in status.
     *
     * @param eventId
     * @param childAdapterPosition
     */
    public void checkIn(int eventId, int childAdapterPosition);

    /**
     * Override to save rsvp status.
     *  @param eventId
     * @param rsvp
     */
    public void rsvp(int eventId, String rsvp);

    void showFullDesc(String description, String title);
}
