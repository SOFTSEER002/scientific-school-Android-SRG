package com.jeannypr.scientificstudy.Login.navigator

import com.jeannypr.scientificstudy.Login.model.ForgetPasswordModel

interface AdmissionNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun redirectToNext(userdetail: ForgetPasswordModel, admNo: String?)

    fun showAdmError(errorMsg: Int)
    fun removeAdmError()

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: Int)

    /**
     * Override to display error in alert.
     */
    fun onError(errorMsg: String)
}
