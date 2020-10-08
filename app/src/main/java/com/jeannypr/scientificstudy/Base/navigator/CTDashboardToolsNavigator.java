package com.jeannypr.scientificstudy.Base.navigator;

public interface CTDashboardToolsNavigator {

    /**
     * Override to navigate to notification screen.
     */
    void redirectToNotification();

    /**
     * Override to navigate to web browser.
     * @param url
     * @param title
     * @param subtitle
     */
    void openInAppBrowser(String url, String title, String subtitle, int errorMsg);

    /**
     * Override to navigate to web browser.
     *
     * @param url
     */
    void openLinkInSystemBrowser(String url, int errorMsg);

    void performSilentLogin(String lessonPlanUrl,Boolean openLinkInWebView);
}
