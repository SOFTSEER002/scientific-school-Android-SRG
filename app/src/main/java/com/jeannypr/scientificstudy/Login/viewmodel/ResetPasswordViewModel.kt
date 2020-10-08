package com.jeannypr.scientificstudy.Login.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.navigator.ResetPasswordNavigator
import com.jeannypr.scientificstudy.R

/**
 *Created by Kannu
 **/
class ResetPasswordViewModel(val mContext: Context, private val userName: String) : BaseViewModel<ResetPasswordNavigator>() {
     var mNewPassword = ObservableField<String>()
     var mOldPassword = ObservableField<String>()
     var mUserName = ObservableField<String>("")

    init {
        mUserName.set(userName)
    }

    /**
     * on click button, read OTP automatically
     * Call api
     * If success then redirect to next screen.
     */
    fun resetPassword() {
        setIsLoading(true)
        navigator!!.disableUserInteraction()

        if (isInputValid()) {
        val mService = DataRepo(LoginService::class.java, mContext).getService()

        //TODO set api format
       /* mService.authenticateUser(mOTP.get(), userAuthenticationDetails.admNo).enqueue(object : Callback<UserAuthenticationBean?> {
            override fun onResponse(call: Call<UserAuthenticationBean?>, response: Response<UserAuthenticationBean?>) {
                setIsLoading(false)
                navigator!!.enableUserInteraction()

                val bean = response.body()
                bean?.let {
                    if (bean.rcode == Constants.Rcode.OK) {
                        val detail = bean.data
                        detail?.let { navigator!!.redirectToNext(detail.userName) }
                    }
                }
            }

            override fun onFailure(call: Call<UserAuthenticationBean?>, t: Throwable) {
                setIsLoading(false)
                navigator!!.enableUserInteraction()
                navigator!!.onError(t.message ?: "")
            }
        }
        )*/


        }
    }

    /**
     * Perform validation
     * Show/remove error
     * @return true if valid else false
     */
    private fun isInputValid(): Boolean {
        return if (mOldPassword.get().isNullOrEmpty()) {
            navigator!!.showOldPassError(R.string.enterOldPassword)
            false
        } else {
            navigator!!.removeOldPassError()
            return if (mNewPassword.get().isNullOrEmpty()) {
                navigator!!.showNewPassError(R.string.enterNewPassword)
                false
            } else {
                navigator!!.removeNewPassError()
                true
            }
        }
    }
}