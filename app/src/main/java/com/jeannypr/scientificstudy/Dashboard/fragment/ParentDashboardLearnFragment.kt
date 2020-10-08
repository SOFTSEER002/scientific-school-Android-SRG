package com.jeannypr.scientificstudy.Dashboard.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel
import com.jeannypr.scientificstudy.Dashboard.model.WebLoginDetailBean
import com.jeannypr.scientificstudy.Dashboard.model.WebLoginDetailModel
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardLearnTabNavigator
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.SilentLogin
import com.jeannypr.scientificstudy.databinding.FragmentLearnTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ParentDashboardLearnFragment : Fragment(), View.OnClickListener {
    private lateinit var userPref: UserPreference
    private var userModel = UserModel()
    private lateinit var loginService: LoginService
    private lateinit var mNavigator: DashboardLearnTabNavigator
    private lateinit var mViewBinding: FragmentLearnTabBinding
    private var mAllModules = ArrayList<DashboardModulesModel>()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is DashboardLearnTabNavigator) {
            mNavigator = activity
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardLearnTabNavigator) {
            mNavigator = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loginService = DataRepo(LoginService::class.java, activity).getService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_learn_tab, container, false)
        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPref = UserPreference.getInstance(activity)
        userModel = userPref.userData

        setClickListners()
    }

    private fun setClickListners() {
        mViewBinding.studyTableBtn.setOnClickListener(this)

        mViewBinding.howToExamsRow.setOnClickListener(this)
        mViewBinding.onlineClsRow.setOnClickListener(this)
        mViewBinding.contactRow.setOnClickListener(this)
        mViewBinding.reachSSRow.setOnClickListener(this)
        mViewBinding.webLoginBtn.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.studyTableBtn -> mNavigator.performSilentLogin(SilentLogin.ONLINE_CLASS_URL_PARENT, false)

            R.id.howToExamsRow -> mNavigator.openInAppBrowser(Constants.HOW_TO_TAKE_EXAM, "", "", R.string.linkNotFoundMsg)
            R.id.onlineClsRow -> mNavigator.openInAppBrowser(Constants.HOW_TO_ATTEND_CLASS, "", "", R.string.linkNotFoundMsg)
            R.id.contactRow -> mNavigator.openLinkInSystemBrowser(userModel.ParentHelpdeskUrl, R.string.linkNotFoundMsg)
            R.id.reachSSRow -> mNavigator.openLinkInSystemBrowser(Constants.REACH_SS, R.string.linkNotFoundMsg)

            R.id.webLoginBtn -> getWebLoginDetails()
        }
    }

    private fun getWebLoginDetails() {
        mViewBinding.pb.visibility = View.VISIBLE
        loginService.getWebLoginDetails(userModel.UserId).enqueue(object : Callback<WebLoginDetailBean?> {
            override fun onResponse(call: Call<WebLoginDetailBean?>, response: Response<WebLoginDetailBean?>) {
                if (response.body() != null) {

                    val bean = response.body()
                    bean?.data?.let {
                        displayData(it)
                    }
                            ?: kotlin.run { Toast.makeText(activity, resources.getString(R.string.technicalError), Toast.LENGTH_SHORT).show() }
                } else Toast.makeText(activity, resources.getString(R.string.technicalError), Toast.LENGTH_SHORT).show()
                mViewBinding.pb.visibility = View.GONE
            }

            override fun onFailure(call: Call<WebLoginDetailBean?>, t: Throwable) {
                mViewBinding.pb.visibility = View.GONE
                Toast.makeText(activity, resources.getString(R.string.technicalError), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun displayData(data: WebLoginDetailModel) {
        mViewBinding.detailSection.visibility = View.VISIBLE
        mViewBinding.loginMessage.setText(data.message)
        mViewBinding.url.setText(data.url)
        mViewBinding.userName.setText(data.userName)
        mViewBinding.password.setText(data.password)
    }
}