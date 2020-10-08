package com.jeannypr.scientificstudy.Login.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordModel
import com.jeannypr.scientificstudy.Login.navigator.ForgetPasswordNavigator
import com.jeannypr.scientificstudy.Login.viewmodel.ForgetPasswordViewModel
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.databinding.ActivityForgetPasswordBinding

/**
 *Created by kannuk on 05/09/2020
 **/
class ForgetPasswordActivity : BaseActivity<ActivityForgetPasswordBinding, ForgetPasswordViewModel>(),
        ForgetPasswordNavigator {
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityForgetPasswordBinding
    private lateinit var mViewModel: ForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mViewBinding = getBinding()
        mViewBinding.viewmodel = mViewModel
        mViewModel.navigator = this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun getViewModel(): ForgetPasswordViewModel {
        mViewModel = ForgetPasswordViewModel(mContext)
        mViewModel = ViewModelProviders.of(this).get(ForgetPasswordViewModel::class.java)
        return mViewModel
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

    override fun redirectToNext(isParentSelected: Boolean) {
        val intent = Intent(mContext, AdmissionActivity::class.java).putExtra(Constants.IS_PARENT, isParentSelected)
        startActivity(intent)
    }

    override fun onError(errorMsg: Int) {

    }

    override fun onError(errorMsg: String) {

    }
}