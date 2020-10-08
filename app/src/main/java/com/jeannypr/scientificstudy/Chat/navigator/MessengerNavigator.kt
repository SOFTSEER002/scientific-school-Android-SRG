package com.jeannypr.scientificstudy.Chat.navigator

interface MessengerNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: Int)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: String)
}