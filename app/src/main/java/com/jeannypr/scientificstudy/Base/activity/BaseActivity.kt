package com.jeannypr.scientificstudy.Base.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.jeannypr.scientificstudy.BackgroundTask.DeviceLogWorker
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService
import com.jeannypr.scientificstudy.Dashboard.model.GrantedModulesBean
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryBean
import com.jeannypr.scientificstudy.Login.viewmodel.BaseViewModel
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.Utilities.LocaleHelper
import com.jeannypr.scientificstudy.application.SptApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseActivity<D : ViewDataBinding, M : BaseViewModel<*>> : AppCompatActivity() {
    protected var mMyApp: SptApp? = null
    private val mCurrentLocale: Locale? = null
    var mNavigator: MainNavigator? = null
    private lateinit var mDataBinding: D
    private var mBaseViewModel: M? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMyApp = this.applicationContext as SptApp

        initializeVM()
        setContentView()
    }

    /**
     * Created to initialize view model.
     */
    private fun initializeVM() {
        mBaseViewModel = if (mBaseViewModel == null) getViewModel() else mBaseViewModel
    }

    /**
     * Override to set view model.
     * @return view model instance of type M.
     */
    abstract fun getViewModel(): M

    /**
     * Created to set content view.
     * Current activity's layout is merged into base layout to inherit navigation activity_drawer.
     */
    private fun setContentView() {
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBinding.executePendingBindings()
    }

    /**
     * Override to get layout id.
     * @return layout id.
     */
    abstract fun getLayoutId(): Int

    /**
     * Override to get instance of view binding.
     * @return instance of view binding.
     */
    fun getBinding(): D {
        return mDataBinding
    }

    override fun onResume() {
        super.onResume()
        mMyApp!!.currentActivity = this
    }

    fun setNavigator(navigator: MainNavigator) {
        this.mNavigator = navigator
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        val currActivity = mMyApp!!.currentActivity
        if (this == currActivity) mMyApp!!.currentActivity = null
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    fun getGrantedFeatures(id: Int, role: String?) {
        val service = DataRepo(AppSettingService::class.java, this).getService()
        val permittedModules = ArrayList<Int>()

        service.GetGrantedFeatures(id, role).enqueue(object : Callback<GrantedModulesBean?> {
            override fun onResponse(call: Call<GrantedModulesBean?>, response: Response<GrantedModulesBean?>) {
                if (response.body() != null) {
                    val bean = response.body()
                    bean?.data?.let{
                        for (datum in bean.data) {
                            permittedModules.add(datum.moduleId)
                        }
                    }
                }
                mNavigator!!.setPermittedModules(permittedModules)
            }

            override fun onFailure(call: Call<GrantedModulesBean?>, t: Throwable) { //TODO: retry network request on failure
                Log.e("Permission", t.message)
            }
        })
    }

    fun GetSessionCurrentState(userId: Int, academicyearId: Int) {
        val service = DataRepo(LoginService::class.java, applicationContext).getService()
        service.CheckSessionExpiry(userId).enqueue(object : Callback<CheckSessionExpiryBean?> {
            override fun onResponse(call: Call<CheckSessionExpiryBean?>, response: Response<CheckSessionExpiryBean?>) {
                if (response.body() != null) {
                    val bean = response.body()
                    if (bean != null && bean.rcode == Constants.Rcode.OK) {
                        val userPref = UserPreference.getInstance(applicationContext)
                        if (bean.data != null) {
                            val expiryModel = bean.data
                            if (expiryModel.isLoginExpired) {
                                userPref.SetIsSessionExpired(true)
                            } else {
                                if (expiryModel.isCurrentAcademicYearId != academicyearId) {
                                    userPref.SetIsSessionExpired(true)
                                } else {
                                    userPref.SetIsSessionExpired(false)
                                }
                            }
                            mNavigator!!.checkSessionExpiry()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CheckSessionExpiryBean?>, t: Throwable) {
                Log.e("Check session validity:", t.message)
            }
        })
    }

    fun SaveDeviceLog() {
        val logConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(DeviceLogWorker::class.java, Constants.DEVICE_LOG_INTERVAL, TimeUnit.DAYS)
                .setConstraints(logConstraints).build()
        WorkManager.getInstance().enqueue(periodicWorkRequest)
    }
}