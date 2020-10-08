package com.jeannypr.scientificstudy.Login.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Login.model.ForgetPasswordModel
import com.jeannypr.scientificstudy.Login.navigator.AuthenticationNavigator
import com.jeannypr.scientificstudy.Login.navigator.OTPListener
import com.jeannypr.scientificstudy.Login.viewmodel.AuthenticationViewModel
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.SMSListener
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityAuthenticateUserBinding


/**
 *Created by Kannu
 **/
class AuthenticationActivity : BaseActivity<ActivityAuthenticateUserBinding, AuthenticationViewModel>(),
        AuthenticationNavigator {
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityAuthenticateUserBinding
    private lateinit var mViewModel: AuthenticationViewModel
    private lateinit var mUserAuthenticationDetails: ForgetPasswordModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mUserAuthenticationDetails = intent.getParcelableExtra(Constants.RESET_PASSWORD_DETAILS)
        super.onCreate(savedInstanceState)
        mContext = this

        mViewBinding = getBinding()
        mViewBinding.viewmodel = mViewModel
        mViewModel.navigator = this

        bindSmsListner()

        mViewBinding.nextBtn.isEnabled = false
//        mViewModel.setIsUserFound(mUserAuthenticationDetails.phoneExists)
        setOTPTextChangeListner()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_authenticate_user
    }

    override fun getViewModel(): AuthenticationViewModel {
        mViewModel = AuthenticationViewModel(mContext, mUserAuthenticationDetails)
        mViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        return mViewModel
    }

    override fun onDestroy() {
        SMSListener.unbindListener()
        super.onDestroy()
    }

    /**
     * bind SMS listner
     * override otp received method
     * bind received OTP to view
     */
    private fun bindSmsListner() {
        SMSListener.bindListener(object : OTPListener {
            override fun onOTPReceived(otp: String) {
                mViewBinding.otpEd.setText(otp)
                mViewModel.authenticateUser()
            }
        })
    }

    /**
     * set OTP TextChangeListner
     * enable button if otp entered else disable the button.
     */
    private fun setOTPTextChangeListner() {
        val mContext = this
        mViewBinding.otpEd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    mViewBinding.nextBtn.isEnabled = !s.isNullOrEmpty()
                } catch (ex: Exception) {
                    Log.e("TextChangedListener: ", ex.message)
                }
            }
        })
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

    override fun redirectToNext(userName: String) {
        val intent = Intent(mContext, ResetPasswordActivity::class.java).putExtra(Constants.USER_NAME, userName)
        startActivity(intent)
    }

    override fun redirectToPrevious() {
        val intent = Intent(mContext, ForgetPasswordActivity::class.java)
        startActivity(intent)
    }

    /**
     * Show popup to display admin contact details
     */
    override fun showAdminDetails() {
        val builder = AlertDialog.Builder(mContext)
        val alertDialog = builder.create()
        alertDialog.setTitle(getString(R.string.alert))

        val alertView = layoutInflater.inflate(R.layout.custom_admin_detail_alert, null, false)
        val alertRow: ConstraintLayout = alertView.findViewById(R.id.alertContainer)

        val positiveBtn = alertRow.findViewById<Button>(R.id.positiveBtn)
        val negativeBtn = alertRow.findViewById<Button>(R.id.negativeBtn)
        val msgTxt: TextView = alertRow.findViewById(R.id.msg)
//        val callBtn: MaterialButton = alertRow.findViewById(R.id.callBtn)
//        val emailbtn: MaterialButton = alertRow.findViewById(R.id.emailBtn)

        msgTxt.setText(R.string.userNotFoundContactAdmin)

        /*callBtn.setOnClickListener {
            // TODO: set click
        }
        emailBtn.setOnClickListener {
            // TODO: set click
        }*/

        negativeBtn.visibility = View.GONE
        positiveBtn.text = resources.getString(R.string.dialogPositiveButtonOk)
        positiveBtn.setOnClickListener {
            alertDialog.dismiss()
            redirectToPrevious()
        }

        alertDialog.setView(alertView)
        alertDialog.show()

        mViewModel.setIsLoading(false)
    }

    override fun readOTP() {
        if (isPermissionGranted()) {
            startCountDownTimer()
        }
    }

    /**
     * Ask user for required permission to read OTP.
     */
    private fun isPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((mContext as Activity), arrayOf(Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS), Constants.PermissionRequestCode.READ_SMS)
        }
        return true
    }

    /**
     * Start timer
     * display time and loader
     */
    private fun startCountDownTimer() {
        object : CountDownTimer(Constants.OTP_EXPIRY_TIME_PERIOD, Constants.OTP_EXPIRY_TIME_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                mViewBinding.autoCapturePB.visibility = View.VISIBLE
                mViewBinding.autoCapture.visibility = View.VISIBLE
                mViewBinding.timer.visibility = View.VISIBLE

                mViewBinding.timer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                //TODO: what if time expires? show "Resend OTP"?
            }
        }.start()
    }
}