package com.jeannypr.scientificstudy.Login.viewmodel

import com.jeannypr.scientificstudy.Login.navigator.ForgetPasswordNavigator
import android.content.Context

/**
 *Created by Kannu
 **/
class ForgetPasswordViewModel(val mContext: Context) : BaseViewModel<ForgetPasswordNavigator>() {

    /**
     * on click role button, redirect to next screen.
     */
    fun redirectToNext(isParentSelected: Boolean) {
        navigator!!.redirectToNext(isParentSelected)
    }
}