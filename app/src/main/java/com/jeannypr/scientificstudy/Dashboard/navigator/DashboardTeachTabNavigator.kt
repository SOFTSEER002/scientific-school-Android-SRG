package com.jeannypr.scientificstudy.Dashboard.navigator

import java.util.*

interface DashboardTeachTabNavigator {
    /**
     * Override to navigate to web browser.
     * @param url
     * @param title
     * @param subtitle
     */
    fun openInAppBrowser(url: String?, title: String?, subtitle: String?, errorMsg: Int)
    /**
     * Override to navigate to web browser.
     *
     * @param url
     */
    fun openLinkInSystemBrowser(url: String?, errorMsg: Int)

    /**
     * Silent login in ERP
     * @param returnUrl Redirct to url
     */
    fun performSilentLogin(returnUrl: String, openLinkInWebView:Boolean)

    fun redirectToTodayTab(tab: Int)

    fun getPermittedModules(): ArrayList<Int>
}