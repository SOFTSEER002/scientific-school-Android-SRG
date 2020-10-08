package com.jeannypr.scientificstudy.Login.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordBean
import com.jeannypr.scientificstudy.Login.navigator.AdmissionNavigator
import com.jeannypr.scientificstudy.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *Created by Kannu
 **/
class AdmissionViewModel(val mContext: Context, isParent: Boolean) : BaseViewModel<AdmissionNavigator>() {
    var mIsParent: Boolean = isParent
    var admNumber = ObservableField<String>()

    /**
     * on click button, redirect to next screen.
     */
    fun redirectToNext() {
        setIsLoading(true)
        navigator!!.disableUserInteraction()

        if (isInputValid()) {
            val mService = DataRepo(LoginService::class.java, mContext).getService()

            mService.getUserDetailsByAdm(admNumber.get()).enqueue(object : Callback<ForgetPasswordBean?> {
                override fun onResponse(call: Call<ForgetPasswordBean?>, response: Response<ForgetPasswordBean?>) {
                    setIsLoading(false)
                    navigator!!.enableUserInteraction()

                    val bean = response.body()
                    bean?.let {
                        if (bean.rcode == Constants.Rcode.OK) {
                            val detail = bean.data
                            if (detail != null) navigator!!.redirectToNext(detail, admNumber.get())
                        }
                    }
                }

                override fun onFailure(call: Call<ForgetPasswordBean?>, t: Throwable) {
                    setIsLoading(false)
                    navigator!!.enableUserInteraction()
                    navigator!!.onError(t.message ?: "")
                }
            }
            )


        }
    }

    /**
     * Perform validation
     * Show/remove error
     * @return true if valid else false
     */
    private fun isInputValid(): Boolean {
        return if (admNumber.get().isNullOrEmpty()) {
            navigator!!.showAdmError(R.string.enterAdm)
            false
        } else {
            navigator!!.removeAdmError()
            true
        }
    }
}