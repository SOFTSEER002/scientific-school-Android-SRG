package com.jeannypr.scientificstudy.Login.navigator

interface AuthenticationNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun redirectToNext(userName: String)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: Int)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: String)

    fun showAdminDetails()
    fun redirectToPrevious()
    fun readOTP()
}
