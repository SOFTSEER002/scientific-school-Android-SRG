package com.jeannypr.scientificstudy.Dashboard.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jeannypr.scientificstudy.Attendance.activity.AttendanceModuleActivity
import com.jeannypr.scientificstudy.Attendance.activity.TakeStudentAttendanceActivity
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity
import com.jeannypr.scientificstudy.Class.activity.ClassListActivity
import com.jeannypr.scientificstudy.Classwork.activity.CwHwListActivity
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesBean
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardTeachTabNavigator
import com.jeannypr.scientificstudy.Login.api.SchoolService
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity
import com.jeannypr.scientificstudy.Syllabus.activity.SyllabusListActivity
import com.jeannypr.scientificstudy.Teacher.activity.MyClassListActivity
import com.jeannypr.scientificstudy.Teacher.activity.TeacherListActivity
import com.jeannypr.scientificstudy.Utilities.SilentLogin
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.FragmentTeachTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DashboardTeachFragment : Fragment(), View.OnClickListener {
    //    lateinit var mContext: Context
    private lateinit var userPref: UserPreference
    private var userModel = UserModel()
    private lateinit var schoolService: SchoolService
    private lateinit var mNavigator: DashboardTeachTabNavigator
    private lateinit var mViewBinding: FragmentTeachTabBinding
    private var mAllModules = ArrayList<DashboardModulesModel>()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is DashboardTeachTabNavigator) {
            mNavigator = activity
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardTeachTabNavigator) {
            mNavigator = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        schoolService = DataRepo(SchoolService::class.java, context).getService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teach_tab, container, false)
        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userPref = UserPreference.getInstance(context)
        userModel = userPref.getUserData()

        if (userModel.RoleTitle.equals(Constants.Role.ADMIN)) getAllModules()
        setClickListners()
        mViewBinding.demo.setOnClickListener(this)
    }

    private fun getAllModules() {
        mViewBinding.pb.visibility = View.VISIBLE
        val service = DataRepo(AppSettingService::class.java, activity).getService()

        service.GetAllModulesList(userModel.RoleTitle).enqueue(object : Callback<DashboardModulesBean?> {
            override fun onResponse(call: Call<DashboardModulesBean?>, response: Response<DashboardModulesBean?>) {
                if (response != null) {
                    if (response.body() != null) {

                        val bean = response.body()
                        if (bean!!.data != null) {
                            mAllModules.clear()
                            mAllModules.addAll(bean.data)
                        }
                    }
                }
                mViewBinding.pb.visibility = View.GONE
            }

            override fun onFailure(call: Call<DashboardModulesBean?>, t: Throwable) {
                mViewBinding.pb.visibility = View.GONE
                Toast.makeText(activity, resources.getString(R.string.technicalError), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setClickListners() {
        mViewBinding.studentRow.setOnClickListener(this)
        mViewBinding.staffRow.setOnClickListener(this)
        mViewBinding.attendanceRow.setOnClickListener(this)

        mViewBinding.noticeRow.setOnClickListener(this)
        mViewBinding.broadcastRow.setOnClickListener(this)
        mViewBinding.chatRow.setOnClickListener(this)

        mViewBinding.LCMRow.setOnClickListener(this)
        mViewBinding.examRow.setOnClickListener(this)
        mViewBinding.homeworkRow.setOnClickListener(this)
        mViewBinding.syllabusRow.setOnClickListener(this)
        mViewBinding.lessonPlanRow.setOnClickListener(this)

        mViewBinding.zoomRow.setOnClickListener(this)
        mViewBinding.larkRow.setOnClickListener(this)
        mViewBinding.googleRow.setOnClickListener(this)
        mViewBinding.msTeamRow.setOnClickListener(this)
        mViewBinding.parentHelpDeskRow.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.studentRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.STUDENT_MODULE))) redirectToStudents()
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else redirectToStudents()

            R.id.staffRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.STAFF_MODULE))) redirectToStaff()
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else
                redirectToStaff()

            R.id.attendanceRow -> {
                if (userModel.RoleTitle == Constants.Role.ADMIN) {
                    if (isModuleGranted(getModuleId(Constants.AdminModules.ATTENDANCE_MODULE))) redirectToAttendance()
                    else
                        Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
                } else if (userModel.RoleTitle == Constants.Role.TEACHER && !userModel.IsClassTeacher)
                    Utility.showAlertDialog(context, null, "", getString(R.string.youCanTake))
                else
                    redirectToAttendance()
            }

            R.id.noticeRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.H_NOTICE))) redirectToNotice()
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else
                redirectToNotice()

            R.id.broadcastRow -> {
                if (userModel.RoleTitle == Constants.Role.ADMIN)
                    redirectToTodayTab()
                else
                    redirectToBroadcast()
            }

            R.id.chatRow -> redirectToChat()

            R.id.LCMRow -> performSilentLogin(SilentLogin.ACADEMIC_CONTENT_MANAGEMENT_URL_TEACHER, false)
            R.id.examRow -> performSilentLogin(SilentLogin.ONLINE_ASSESSMENT_URL_TEACHER, false)

            R.id.homeworkRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.HW_MODULE))) redirectToHomework()
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else
                redirectToHomework()

            R.id.syllabusRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.SYLLABUS))) redirectToSyllabus()
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else
                redirectToSyllabus()

            R.id.lessonPlanRow -> if (userModel.RoleTitle == Constants.Role.ADMIN) {
                if (isModuleGranted(getModuleId(Constants.AdminModules.LESSON_PLAN))) performSilentLogin(SilentLogin.LESSON_PLAN_URL, false)
                else
                    Utility.showAlertDialog(context, null, context?.resources?.getString(R.string.moduleNotGrantedHeader), context?.resources?.getString(R.string.moduleNotGrantedMsg))
            } else
                performSilentLogin(SilentLogin.LESSON_PLAN_URL, false)

            R.id.zoomRow -> redirectToZoom()
            R.id.larkRow -> redirectToLark()
            R.id.googleRow -> redirectToGoogleMeet()
            R.id.msTeamRow -> Utility.openInAppOrDownload(context, Constants.MS_TEAM_PACKAGE_ID)
            R.id.parentHelpDeskRow -> mNavigator.openLinkInSystemBrowser(Constants.HELP_ARTICLES_URL, R.string.linkNotFoundMsg)

            R.id.demo -> {
             /*   val chatIntent = Intent(activity, MainActivity::class.java)
                startActivity(chatIntent)*/
            }
        }
    }

    private fun getModuleId(moduleName: String): Int {
        for (module in mAllModules) {
            if (module.moduleName == moduleName) return module.moduleId
        }
        return -1
    }

    private fun isModuleGranted(selectedModuleId: Int): Boolean {
        val permittedModules = getPermittedModules()
        if (permittedModules.size > 0) {
            for (permittedModuleId in permittedModules) {
                if (permittedModuleId == selectedModuleId) return true
            }
        }
        return false
    }

    private fun getPermittedModules(): java.util.ArrayList<Int> {
        return mNavigator.getPermittedModules()
    }

    private fun redirectToTodayTab() {
        mNavigator.redirectToTodayTab(Constants.AdminDashboardBottomNavTab.TODAY)
//        val fragmentManager = activity?.supportFragmentManager
//        val ft = fragmentManager?.beginTransaction()
//        val fragment = AdminDashboardTodayFragment(this as activity)
//        ft?.replace(R.id.fragment_container, fragment, userModel.RoleTitle)
//        ft?.commit()
    }

    private fun redirectToSyllabus() {
        val chatIntent = Intent(activity, SyllabusListActivity::class.java)
        startActivity(chatIntent)
    }

    private fun redirectToZoom() {
//        mNavigator.openInAppBrowser(Constants.ZOOM_URL, null, null, R.string.somethingWrongMsg)
        Utility.openInAppOrWeb(activity, Constants.ZOOM_PACKAGE, Constants.ZOOM_URL, null, null, R.string.somethingWrongMsg)
    }

    private fun redirectToLark() {
//        mNavigator.openInAppBrowser(Constants.LARK_URL, null, null, R.string.somethingWrongMsg)
        Utility.openInAppOrWeb(activity, Constants.LARK_PACKAGE, Constants.LARK_URL, null, null, R.string.somethingWrongMsg)
    }

    private fun redirectToGoogleMeet() {
//        mNavigator.openInAppBrowser(Constants.GOOGLE_MEET_URL, null, null, R.string.somethingWrongMsg)
        Utility.openInAppOrWeb(activity, Constants.GOOGLE_MEET_PACKAGE, Constants.GOOGLE_MEET_URL, null, null, R.string.somethingWrongMsg)
    }

    private fun performSilentLogin(url: String, openLinkInWebView: Boolean) {
        mNavigator.performSilentLogin(url, openLinkInWebView)
    }

    private fun redirectToHomework() {
        val intent = Intent(activity, CwHwListActivity::class.java)
        intent.putExtra(Constants.ACTIVITY_TYPE, Constants.DiaryType.Homework)
        startActivity(intent)
    }

    private fun redirectToChat() {
        val chatIntent = Intent(context, ChatGroupActivity::class.java)
        startActivity(chatIntent)
    }

    private fun redirectToNotice() {
        val intent = Intent(activity, SptWallActivity::class.java).putExtra("news", Constants.PostType.NOTICE)
        startActivity(intent)
    }

    private fun redirectToBroadcast() {
        Utility.showAlertDialog(context, null, "", getString(R.string.comingSoon))
    }

    private fun redirectToStaff() {
        val intent = Intent(activity, TeacherListActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToAttendance() {
        val intent = if (userModel.RoleTitle == Constants.Role.ADMIN) Intent(activity, AttendanceModuleActivity::class.java)
        else Intent(context, TakeStudentAttendanceActivity::class.java).putExtra("ClassId", userModel.ClassId).putExtra("ClassName", userModel.ClassName)
        startActivity(intent)
    }

    private fun redirectToStudents() {
        val intent = if (userModel.RoleTitle == Constants.Role.ADMIN) Intent(activity, ClassListActivity::class.java)
        else Intent(activity, MyClassListActivity::class.java)
        startActivity(intent)
    }

    private fun displayErrorMsg(errorMsg: Int) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }


}