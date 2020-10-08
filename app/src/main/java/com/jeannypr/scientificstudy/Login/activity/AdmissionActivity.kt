package com.jeannypr.scientificstudy.Login.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordModel
import com.jeannypr.scientificstudy.Login.navigator.AdmissionNavigator
import com.jeannypr.scientificstudy.Login.viewmodel.AdmissionViewModel
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityAdmissionBinding

/**
 *Created by Kannu
 **/
class AdmissionActivity : BaseActivity<ActivityAdmissionBinding, AdmissionViewModel>(),
        AdmissionNavigator {
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityAdmissionBinding
    private lateinit var mViewModel: AdmissionViewModel
    private var isParent = false

    override fun onCreate(savedInstanceState: Bundle?) {
         isParent = intent.getBooleanExtra(Constants.IS_PARENT, false)
        super.onCreate(savedInstanceState)
        mContext = this

        mViewBinding = getBinding()
        mViewBinding.viewmodel = mViewModel
        mViewModel.navigator = this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admission
    }

    override fun getViewModel(): AdmissionViewModel {
        mViewModel = AdmissionViewModel(mContext, isParent)
        mViewModel = ViewModelProviders.of(this).get(AdmissionViewModel::class.java)
        return mViewModel
    }

    override fun redirectToNext(userdetail: ForgetPasswordModel, admNo: String?) {
        userdetail.admNo = admNo;
        val intent = Intent(this, AuthenticationActivity::class.java).putExtra(Constants.RESET_PASSWORD_DETAILS, userdetail)
        startActivity(intent)
    }

    /**
     * Disable user interaction.
     */
    override fun disableUserInteraction() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Enable user interaction.
     */
    override fun enableUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * Display error in alert
     * @param errorMsg
     */
    override fun onError(errorMsg: Int) {
        val error: Int = if (errorMsg == 0) R.string.somethingWrongMsg
        else errorMsg
        Utility.showAlertDialog(
                this, null, "", getString(error)
        )
    }

    override fun onError(errorMsg: String) {
        Utility.showAlertDialog(
                this, null, "", errorMsg
        )
    }

    override fun showAdmError(errorMsg: Int) {
        mViewBinding.admNoTv.error = getString(errorMsg)
    }

    override fun removeAdmError() {
        mViewBinding.admNoTv.error = null
    }
}