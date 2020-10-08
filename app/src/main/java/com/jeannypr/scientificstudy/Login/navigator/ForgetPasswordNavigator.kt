package com.jeannypr.scientificstudy.Login.navigator

interface ForgetPasswordNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun redirectToNext(isParentSelected: Boolean)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: Int)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: String)
}
