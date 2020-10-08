package com.jeannypr.scientificstudy.Login.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Login.navigator.ResetPasswordNavigator
import com.jeannypr.scientificstudy.Login.viewmodel.ResetPasswordViewModel
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityResetPasswordBinding

/**
 *Created by Kannu
 **/
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>(),
        ResetPasswordNavigator {
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityResetPasswordBinding
    private lateinit var mViewModel: ResetPasswordViewModel
    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        super.onCreate(savedInstanceState)
        mContext = this

        mViewBinding = getBinding()
        mViewBinding.viewmodel = mViewModel
        mViewModel.navigator = this
        setTextChangeListner()
    }

    private fun setTextChangeListner() {
        mViewBinding.newPasswordEd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    //TODO check if already available, set eye icon also
//                    s?.let { if (!s.toString().equals(mViewModel.mOldPassword.get())) showPasswordError(R.string.passwordMismatch) else hidePasswordError() }
                } catch (ex: Exception) {
                    Log.e("TextChangedListener: ", ex.message)
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
    }

    override fun getViewModel(): ResetPasswordViewModel {
        mViewModel = ResetPasswordViewModel(mContext, mUserName)
        mViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)
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

    override fun redirectToNext() {
//        val intent = Intent(mContext, ResetPasswordActivity::class.java)
//        startActivity(intent)
    }

    override fun removeNewPassError() {
        mViewBinding.newPasswordEd.error = null
    }

    override fun showNewPassError(errorMsg: Int) {
        mViewBinding.newPasswordTv.isErrorEnabled = true
        mViewBinding.newPasswordEd.error = getString(errorMsg)
    }

    override fun removeOldPassError() {
        mViewBinding.oldPasswordEd.error = null
    }

    override fun showOldPassError(errorMsg: Int) {
        mViewBinding.oldPasswordTv.isErrorEnabled = true
        mViewBinding.oldPasswordEd.error = getString(errorMsg)
    }
}