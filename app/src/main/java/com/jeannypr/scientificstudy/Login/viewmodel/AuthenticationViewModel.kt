package com.jeannypr.scientificstudy.Login.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordModel
import com.jeannypr.scientificstudy.Login.model.UserAuthenticationBean
import com.jeannypr.scientificstudy.Login.navigator.AuthenticationNavigator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *Created by Kannu
 **/
class AuthenticationViewModel(val mContext: Context, private val userAuthenticationDetails: ForgetPasswordModel) : BaseViewModel<AuthenticationNavigator>() {
    var mOTP = ObservableField<String>()
    private var mIsUserFound = ObservableBoolean(true)

    init {
        setIsUserFound(userAuthenticationDetails.phoneExists)
    }

    /**
     * on click role button, redirect to next screen.
     * @param isUserFound if user details not found then show admin contact details else read OTP.
     */
    private fun setIsUserFound(isUserFound: Boolean) {
        setIsLoading(true)
        mIsUserFound.set(isUserFound)
        if (!isUserFound)
            navigator!!.showAdminDetails()
        else navigator!!.readOTP()
    }

    /**
     * on click button, read OTP automatically
     * Call api
     * If success then redirect to next screen.
     */
    fun authenticateUser() {
        setIsLoading(true)
        navigator!!.disableUserInteraction()

//        if (isInputValid()) {
        val mService = DataRepo(LoginService::class.java, mContext).getService()

        //TODO set api format
        mService.authenticateUser(mOTP.get(), userAuthenticationDetails.admNo).enqueue(object : Callback<UserAuthenticationBean?> {
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
        )


//        }
    }

    /**
     * Perform validation
     * Show/remove error
     * @return true if valid else false
     */
    /*private fun isInputValid(): Boolean {
        return if (mOTP.get().isNullOrEmpty()) {
            navigator!!.showAdmError(R.string.enterAdm)
            false
        } else {
            navigator!!.removeAdmError()
            true
        }
    }*/
}