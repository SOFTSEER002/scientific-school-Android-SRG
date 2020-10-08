package com.jeannypr.scientificstudy.Login.navigator

interface ResetPasswordNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun redirectToNext()

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: Int)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: String)

    fun removeNewPassError()
    fun showNewPassError(errorMsg: Int)
    fun removeOldPassError()
    fun showOldPassError(errorMsg: Int)
}
