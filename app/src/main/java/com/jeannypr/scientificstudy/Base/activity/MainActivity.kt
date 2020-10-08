package com.jeannypr.scientificstudy.Base.activity

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import com.jeannypr.scientificstudy.BackgroundTask.DeviceAccessLogWorker
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.Bean
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Base.RequestCodes
import com.jeannypr.scientificstudy.Base.navigator.CTDashboardToolsNavigator
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator
import com.jeannypr.scientificstudy.Base.navigator.ParentDashboardToolsNavigator
import com.jeannypr.scientificstudy.Base.viewmodel.MainViewModel
import com.jeannypr.scientificstudy.BuildConfig
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity
import com.jeannypr.scientificstudy.Chat.api.ChatService
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService
import com.jeannypr.scientificstudy.Dashboard.fragment.*
import com.jeannypr.scientificstudy.Dashboard.fragment.AdminDashboardFragment.AdminDashboardNavigator
import com.jeannypr.scientificstudy.Dashboard.fragment.DriverDashboardHomeFragment.CommunicateWithActivity
import com.jeannypr.scientificstudy.Dashboard.model.ReminderRequest
import com.jeannypr.scientificstudy.Dashboard.model.SettingBean
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardLearnTabNavigator
import com.jeannypr.scientificstudy.Dashboard.navigator.DashboardTeachTabNavigator
import com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton
import com.jeannypr.scientificstudy.Login.activity.EnterSchoolKeyActivity
import com.jeannypr.scientificstudy.Login.activity.LoginActivity
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.model.CheckAppVersionBean
import com.jeannypr.scientificstudy.Login.model.FedratedLoginBean
import com.jeannypr.scientificstudy.Login.model.FedratedLoginInput
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.*
import com.jeannypr.scientificstudy.Utilities.ForceUpdateChecker.OnUpdateNeededListener
import com.jeannypr.scientificstudy.databinding.ActivityMain2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMain2Binding, MainViewModel>(),
        NavigationView.OnNavigationItemSelectedListener,
        OnUpdateNeededListener, AdminDashboardNavigator,
        MainNavigator, BroadcastMsgFragment.OnFragmentInteractionListener,
        ParentDashboardToolsNavigator, CTDashboardToolsNavigator,
        DashboardTeachTabNavigator,
        DashboardLearnTabNavigator {

    lateinit var context: Context
    lateinit var userPref: UserPreference
    private var userModel = UserModel()
    lateinit var schoolDetailModel: SchoolDetailModel
    var role: String? = null
    var chatService: ChatService? = null
    private var workManager: WorkManager? = null
    var topicName: String? = null
    lateinit var bnv: BottomNavigationView
    lateinit var fab: MovableFloatingActionButton
    var p_dialog: ProgressDialog? = null
    var fragContainer: FrameLayout? = null
    var selectedTab = 0
    lateinit var fragment: Fragment
    var providerChangeReceiver: BroadcastReceiver? = null
    var mPermittedModules = ArrayList<Int>()
    /*Fused*/
    private val googleApiClient: GoogleApiClient? = null
    // lists for permissions
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected = ArrayList<String>()
    private val permissions = ArrayList<String>()
    var locationCallback: LocationCallback? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private var isGPS = false
    private var reminderDate: String? = null
    private lateinit var mViewBinding: ActivityMain2Binding
    private lateinit var mViewModel: MainViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        setNavigator(this)
        userPref = UserPreference.getInstance(context)
        workManager = WorkManager.getInstance()

        userPref.SetCurrentChatRoomId(0)
        SaveDeviceLog()
        /*
          Check if user is logged in
         */
        if (!userPref.isLoggedIn) {
            if (userPref.isSchoolKeyAvailable) {
                redirectToLogin()
            } else {
                redirectToEnterKey()
            }
        } else {
            chatService = DataRepo(ChatService::class.java, context, ApiConstants.CHAT_BASE_URL).getService()
            schoolDetailModel = userPref.schoolData
            userModel = userPref.userData
            Utility.GetLocationPermissn(context)

            if (Utility.isConnectedToInternet(context)) {
                GetSessionCurrentState(userModel.UserId, userModel.AcademicyearId)
                ForceUpdateChecker.with(context).onUpdateNeeded(this).check()
                getGrantedFeatures(userModel.UserId, userModel.RoleTitle)
                checkSessionExpiry()

            } else displayErrorMsg(R.string.noInternetMsg)
            topicName = if (userPref.getUserData().RoleTitle == Constants.Role.PARENT) schoolDetailModel.getSchoolKey() + "_" + Constants.Role.PARENT
            else schoolDetailModel.getSchoolKey() + "_" + Constants.Role.TEACHER
            userPref.SetSubscriptionTopic(topicName)
            subscribeTopic()
            saveDeviceAccessLog()
            initializeViews()
            setAlarmToCheckSession(Constants.Alarm.START);
            checkForUpdate(context, true)
        }
    }

    override fun getViewModel(): MainViewModel {
        mViewModel = MainViewModel()
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main2
    }

    /**
     * Check if user logged in & session expired
     * Then logout from app
     * Stop the service.
     */
    override fun checkSessionExpiry() {
        val pref: UserPreference = userPref
        if (pref.isLoggedIn) {
            if (pref.IsSessionExpired()) {
                setAlarmToCheckSession(Constants.Alarm.CANCEL);
                Logout(pref.userData, pref.schoolCode)
            }
        }
    }

    /**
     * set Alarm To Check Session
     * CheckSessionService will be called in specific interval to check session
     * @param operationType start or stop the Alarm.
     */
    private fun setAlarmToCheckSession(operationType: Int) {
        //set alarm on login to check whether session has expired or not, if expired then logout.
        try {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.add(Calendar.MINUTE, 5)

            val alarmIntent = Intent(context, CheckSessionService::class.java)
            alarmIntent.putExtra("requestCode", RequestCodes.CLOSE_ALARM_ON_LOGOUT_REQUEST_CODE)
            val pi = PendingIntent.getService(context, 0, alarmIntent, 0)

            Utility.SetAlarm(operationType, context, pi, cal.getTimeInMillis(), 5000) //60000
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /*  private fun SetWorkerToCheckSession(operationType: Int) {
         val logConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
         val checkSessionRequest = PeriodicWorkRequest.Builder(CheckSessionWorker::class.java, 15, TimeUnit.MINUTES)
                 .setConstraints(logConstraints).build()
         workManager!!.enqueue(checkSessionRequest)
     }*/

    private fun displayErrorMsg(errorMsg: Int) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        if (Utility.isConnectedToInternet(context)) {
            checkSessionExpiry()
            getGrantedFeatures(userModel.UserId, userModel.RoleTitle)

        } else displayErrorMsg(R.string.noInternetMsg)
        if (role != null && role == Constants.Role.DRIVER) {
            registerReceiver(providerChangeReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
            val journeyId = userPref.journeyDetail.journeyId
            if (journeyId != 0) {
                StartLocationService()
            }
        }
    }

    private fun initializeViews() {
        fragContainer = findViewById(R.id.fragment_container)
        val toolbar = findViewById<Toolbar>(R.id.dashboardToolbar)

        setSupportActionBar(toolbar)
        Utility.SetToolbar(context, schoolDetailModel.SchoolName, "")

        bnv = findViewById(R.id.bottomNav)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, ChatGroupActivity::class.java)
            startActivity(intent)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val versionTxt = findViewById<TextView>(R.id.version)
        versionTxt.text = "Version : " + BuildConfig.VERSION_NAME
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val header = navigationView.getHeaderView(0)
        val schoolName = header.findViewById<TextView>(R.id.schoolName)
        val academicYear = header.findViewById<TextView>(R.id.academicYear)
        val userName = header.findViewById<TextView>(R.id.loggedInUserName)
        val schoolLogo = header.findViewById<ImageView>(R.id.schoolLogo)

        schoolName.text = schoolDetailModel.SchoolName
        academicYear.text = userModel.AcademicYearName
        userName.text = userModel.FirstName
        role = userModel.RoleTitle

        if (schoolDetailModel.SchoolLogo != null && schoolDetailModel.SchoolLogo != "")
            Glide.with(context).load(schoolDetailModel.SchoolLogo).into(schoolLogo)

        if (role == Constants.Role.DRIVER) {
            fab?.hide()
            initializeDriversBnv()
            initializeProviderReceiver()

            if (userPref.IsJourneyStarted()) {
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.NOTIFY).isVisible = true
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT).isVisible = true
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.STUDENT).isVisible = true
            } else {
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.NOTIFY).isVisible = false
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT).isVisible = false
                bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.STUDENT).isVisible = false
            }

        } else {
            if (role == Constants.Role.ADMIN) {
                initializeAdminsBnv()
            } else if (role == Constants.Role.PARENT) {
                initializeParentsBnv()
            } else if (role == Constants.Role.TEACHER && userModel.ClassId != null && userModel.ClassId != -1) {
                initializeCTBnv()
            } else {
                initializeSTBnv()
            }
            if (LocaleHelper.getLanguage(context) != Constants.LanguagesISOCode.ENGLISH) {
                LocaleHelper.setLanguage(context, Constants.LanguagesISOCode.ENGLISH)
                relaunch(context as Activity?)
            }
        }
        setBnvListner()
    }

    private fun initializeProviderReceiver() {
        providerChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                checkGpsStatus()
            }
        }
        registerReceiver(providerChangeReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    private fun checkGpsStatus() {
        GpsUtils(context).turnGPSOn { isGPSEnable ->
            // turn on GPS
            val journeyId = userPref.journeyDetail.journeyId
            isGPS = isGPSEnable
            if (!isGPS) {
                if (journeyId != 0)
                    Toast.makeText(context, "Please turn on GPS otherwise journey will be closed.", Toast.LENGTH_SHORT).show()

            } else {
                if (journeyId != 0)  //TrackLocationTask.getInstance(context).TrackLocation(journeyId, Constants.JourneyMode.START, userModel.getSchoolId());
                    StartLocationService()

            }
        }
    }

    private fun relaunch(activity: Activity?) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity!!.startActivity(intent)
        //  Runtime.getRuntime().exit(0);
        activity.finish()
        overridePendingTransition(0, 0)
    }

    private fun initializeDriversBnv() {
        addMenusInBnv(Menu.NONE, Constants.DriverDashboardBottomNavTab.HOME,
                1, resources.getString(R.string.driverDashboardHomeTab),
                R.drawable.ic_home_dark)
        addMenusInBnv(Menu.NONE, Constants.DriverDashboardBottomNavTab.LOGOUT,
                5, resources.getString(R.string.driverDashboardLogoutTab),
                android.R.drawable.ic_lock_power_off)
        addMenusInBnv(Menu.NONE, Constants.DriverDashboardBottomNavTab.NOTIFY,
                2, resources.getString(R.string.driverDashboardNotifyTab),
                R.drawable.ic_notification)
        addMenusInBnv(Menu.NONE, Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT,
                4, resources.getString(R.string.driverDashboardEmergancyContact),
                R.drawable.emergency_call)
        addMenusInBnv(Menu.NONE, Constants.DriverDashboardBottomNavTab.STUDENT,
                3, resources.getString(R.string.driverdashboardStudent), R.drawable.student_wise_attendance)
        switchFragment(Constants.DriverDashboardBottomNavTab.HOME)
    }

    private fun initializeCTBnv() {
        addMenusInBnv(Menu.NONE, Constants.CTDashboardBottomNavTab.HOME,
                1, resources.getString(R.string.adminDashboardHomeTab),
                R.mipmap.home_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.CTDashboardBottomNavTab.TEACH,
                2, resources.getString(R.string.adminDashboardTeachTab),
                R.drawable.ic_teach_tab)
        addMenusInBnv(Menu.NONE, Constants.CTDashboardBottomNavTab.TOOLS,
                3, resources.getString(R.string.adminDashboardToolsTab),
                R.mipmap.tools_tab_ic)
        switchFragment(Constants.CTDashboardBottomNavTab.HOME)
    }

    private fun initializeSTBnv() {
        addMenusInBnv(Menu.NONE, Constants.STDashboardBottomNavTab.HOME,
                1, resources.getString(R.string.adminDashboardHomeTab),
                R.mipmap.home_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.STDashboardBottomNavTab.TEACH,
                2, resources.getString(R.string.adminDashboardTeachTab),
                R.drawable.ic_teach_tab)
        addMenusInBnv(Menu.NONE, Constants.STDashboardBottomNavTab.TOOLS,
                3, resources.getString(R.string.adminDashboardToolsTab),
                R.mipmap.tools_tab_ic)
        switchFragment(Constants.STDashboardBottomNavTab.HOME)
    }

    private fun initializeAdminsBnv() {
        addMenusInBnv(Menu.NONE, Constants.AdminDashboardBottomNavTab.HOME,
                1, resources.getString(R.string.adminDashboardHomeTab),
                R.mipmap.home_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.AdminDashboardBottomNavTab.TODAY,
                2, resources.getString(R.string.adminDashboardTodayTab),
                R.mipmap.today_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.AdminDashboardBottomNavTab.TEACH,
                3, resources.getString(R.string.adminDashboardTeachTab),
                R.drawable.ic_teach_tab)
        addMenusInBnv(Menu.NONE, Constants.AdminDashboardBottomNavTab.TOOLS,
                4, resources.getString(R.string.adminDashboardToolsTab),
                R.mipmap.tools_tab_ic)
        switchFragment(Constants.AdminDashboardBottomNavTab.HOME)
    }

    private fun initializeParentsBnv() {
        addMenusInBnv(Menu.NONE, Constants.ParentDashboardBottomNavTab.HOME,
                1, resources.getString(R.string.adminDashboardHomeTab),
                R.mipmap.home_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.ParentDashboardBottomNavTab.TOOLS,
                3, resources.getString(R.string.adminDashboardToolsTab),
                R.mipmap.tools_tab_ic)
        addMenusInBnv(Menu.NONE, Constants.ParentDashboardBottomNavTab.LEARN,
                2, getResources().getString(R.string.parentDashboardLearnTab),
                R.drawable.learn_tab_ic2)

        /* AddMenusInBnv(Menu.NONE, Constants.ParentDashboardBottomNavTab.COMMUNITY,
                 4, getResources().getString(R.string.parentDashboardCommunityTab),
                 R.mipmap.tools_tab_ic);*/
        switchFragment(Constants.AdminDashboardBottomNavTab.HOME)
    }

    private fun addMenusInBnv(groupId: Int, menuId: Int, order: Int, menuTitle: String, icon: Int) {
        val menu = bnv.menu
        menu.add(groupId, menuId, order, menuTitle)
        menu.findItem(menuId).setIcon(icon)
    }

    private fun setBnvListner() {
        bnv.setOnNavigationItemSelectedListener { menuItem ->
            if (role == Constants.Role.DRIVER) {
                when (menuItem.itemId) {
                    Constants.DriverDashboardBottomNavTab.NOTIFY -> {
                        selectedTab = Constants.DriverDashboardBottomNavTab.NOTIFY
                        switchFragment(selectedTab)
                    }
                    Constants.DriverDashboardBottomNavTab.LOGOUT -> logoutDriver()
                    Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT -> {
                        selectedTab = Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT
                        switchFragment(selectedTab)
                    }
                    Constants.DriverDashboardBottomNavTab.STUDENT -> {
                        selectedTab = Constants.DriverDashboardBottomNavTab.STUDENT
                        switchFragment(selectedTab)
                    }
                    else -> {
                        selectedTab = Constants.DriverDashboardBottomNavTab.HOME
                        switchFragment(selectedTab)
                    }
                }
            } else if (role == Constants.Role.ADMIN) {
                when (menuItem.itemId) {
                    Constants.AdminDashboardBottomNavTab.TODAY -> {
                        selectedTab = Constants.AdminDashboardBottomNavTab.TODAY
                        switchFragment(selectedTab)
                    }
                    Constants.AdminDashboardBottomNavTab.TOOLS -> {
                        selectedTab = Constants.AdminDashboardBottomNavTab.TOOLS
                        switchFragment(selectedTab)
                    }
                    Constants.AdminDashboardBottomNavTab.TEACH -> {
                        selectedTab = Constants.AdminDashboardBottomNavTab.TEACH
                        switchFragment(selectedTab)
                    }
                    else -> {
                        selectedTab = Constants.AdminDashboardBottomNavTab.HOME
                        switchFragment(selectedTab)
                    }
                }
            } else if (role == Constants.Role.PARENT) {
                when (menuItem.itemId) {
                    Constants.ParentDashboardBottomNavTab.TOOLS -> {
                        selectedTab = Constants.ParentDashboardBottomNavTab.TOOLS
                        switchFragment(selectedTab)
                    }
                    Constants.ParentDashboardBottomNavTab.HOME -> {
                        selectedTab = Constants.ParentDashboardBottomNavTab.HOME
                        switchFragment(selectedTab)
                    }
                    Constants.ParentDashboardBottomNavTab.LEARN -> {
                         selectedTab = Constants.ParentDashboardBottomNavTab.LEARN
                         switchFragment(selectedTab)
                     }
                    /* else -> {
                        selectedTab = Constants.ParentDashboardBottomNavTab.COMMUNITY
                        SwitchFragment(selectedTab)
                    }*/
                }
            } else if (role == Constants.Role.TEACHER && userModel.ClassId != null && userModel.ClassId != -1) {
                when (menuItem.itemId) {
                    Constants.CTDashboardBottomNavTab.TOOLS -> {
                        selectedTab = Constants.CTDashboardBottomNavTab.TOOLS
                        switchFragment(selectedTab)
                    }
                    Constants.CTDashboardBottomNavTab.TEACH -> {
                        selectedTab = Constants.CTDashboardBottomNavTab.TEACH
                        switchFragment(selectedTab)
                    }
                    else -> {
                        selectedTab = Constants.CTDashboardBottomNavTab.HOME
                        switchFragment(selectedTab)
                    }
                }
            } else {
                when (menuItem.itemId) {
                    Constants.STDashboardBottomNavTab.TOOLS -> {
                        selectedTab = Constants.STDashboardBottomNavTab.TOOLS
                        switchFragment(selectedTab)
                    }
                    Constants.STDashboardBottomNavTab.TEACH -> {
                        selectedTab = Constants.STDashboardBottomNavTab.TEACH
                        switchFragment(selectedTab)
                    }
                    else -> {
                        selectedTab = Constants.STDashboardBottomNavTab.HOME
                        switchFragment(selectedTab)
                    }
                }
            }
            true
        }
    }

    private fun logoutDriver() {
        if (!userPref.IsJourneyStarted())
            switchFragment(Constants.DriverDashboardBottomNavTab.LOGOUT)
        else { /* AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(R.string.dialogTitle)
                    .setMessage(R.string.dialogMsg)
                    .setPositiveButton(R.string.dialogButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();*/
            val view = LayoutInflater.from(context).inflate(R.layout.row_alert_dialog_buttons, fragContainer, false)
            val dialog = AlertDialog.Builder(context)
                    .setTitle(R.string.dialogTitle)
                    .setMessage(R.string.dialogMsg) //   .setPositiveButton(R.string.dialogPositiveButton, null)
                    .setView(view)
                    .show()
            val positiveBtn = view.findViewById<Button>(R.id.positiveBtn)
            view.findViewById<View>(R.id.negativeBtn).visibility = View.GONE
            positiveBtn.text = resources.getString(R.string.dialogPositiveButtonOk)
            /*  positiveBtn.setBackgroundColor(Color.TRANSPARENT);
                positiveBtn.setTextColor(getResources().getColor(R.color.colorPrimary));*/
//  positiveBtn.setBackgroundColor(getResources().getColor(R.color.red));
// positiveBtn.setBackground(getResources().getDrawable(R.drawable.white_bg_round_corner));
//   positiveBtn.setBackgroundResource(R.drawable.white_border_white_bg);
            positiveBtn.setOnClickListener { dialog.dismiss() }
        }
    }

    private fun saveDeviceAccessLog() {
        val logConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest: PeriodicWorkRequest =
                PeriodicWorkRequest.Builder(DeviceAccessLogWorker::class.java, Constants.DEVICE_ACCESS_LOG_INTERVAL, TimeUnit.DAYS)
                        .setConstraints(logConstraints).build()
        workManager!!.enqueue(periodicWorkRequest)
    }

    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(topicName!!)
        FirebaseMessaging.getInstance().subscribeToTopic("SPTALL")
    }

    private fun unSubscribeFromTopic(topic: String?) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic!!)
            FirebaseMessaging.getInstance().unsubscribeFromTopic("SPTALL")
            /*  .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Log.d("FRBT", "SuccessFully unsubscribed");
                            }
                        }
                    });*/
        } catch (ex: Exception) {
            Log.e("Unsubscribe topic ;", ex.message)
        }
    }

    private fun switchFragment(tab: Int) {
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()

        when (val role = userModel.RoleTitle) {
            Constants.Role.ADMIN -> when (tab) {
                Constants.AdminDashboardBottomNavTab.TODAY -> {
                    fab.hide()
                    fragment = AdminDashboardTodayFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
                Constants.AdminDashboardBottomNavTab.TOOLS -> {
                    fab.show();
                    fragment = AdminDashboardFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
                Constants.AdminDashboardBottomNavTab.TEACH -> {
                    fab.hide()
                    fragment = DashboardTeachFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
                else -> {
                    fab.hide()
                    fragment = AdminDashboardHomeFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
            }

            Constants.Role.PARENT -> when (tab) {
                Constants.ParentDashboardBottomNavTab.TOOLS -> {
                    fab.show();
                    fragment = ParentDashboardFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
                Constants.ParentDashboardBottomNavTab.COMMUNITY -> {
                    fab.hide()
                    fragment = CommunityDashboardFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
                 Constants.ParentDashboardBottomNavTab.LEARN -> {
                     fab?.hide()
                     fragment = ParentDashboardLearnFragment()
                     ft.replace(R.id.fragment_container, fragment, role)
                     ft.commit()
                 }
                else -> {
                    fab.hide()
                    fragment = ParentDashboardHomeFragment()
                    ft.replace(R.id.fragment_container, fragment, role)
                    ft.commit()
                }
            }

            Constants.Role.TEACHER -> if (userModel.ClassId == null || userModel.ClassId == -1) {
                when (tab) {
                    Constants.STDashboardBottomNavTab.TOOLS -> {
                        fab.show()
                        fragment = SubjectTeacherDashboardFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    Constants.STDashboardBottomNavTab.TEACH -> {
                        fab.hide()
                        fragment = DashboardTeachFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    else -> {
                        fab.hide()
                        fragment = SubjectTeacherDashboardHomeFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                }
            } else {
                when (tab) {
                    Constants.CTDashboardBottomNavTab.TOOLS -> {
                        fab.show()
                        fragment = ClassTeacherDashboardFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    Constants.CTDashboardBottomNavTab.TEACH -> {
                        fab.hide()
                        fragment = DashboardTeachFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    else -> {
                        fab.hide()
                        fragment = ClassTeacherDashboardHomeFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                }
            }
            Constants.Role.DRIVER -> {
                fab.hide()
                when (tab) {
                    Constants.DriverDashboardBottomNavTab.HOME -> {
                        fragment = DriverDashboardHomeFragment(
                                object : CommunicateWithActivity {
                                    override fun OnChangeLanguage(ISOCode: String) {
                                        LocaleHelper.setLanguage(context, ISOCode)
                                        relaunch(context as Activity?)
                                    }

                                    override fun OnCompleteJourney() {
                                        if (!(context as Activity?)!!.isFinishing) {
                                            bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.NOTIFY).isVisible = false
                                            bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT).isVisible = false
                                            bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.STUDENT).isVisible = false
                                            //SetAlarm(Constants.Alarm.CANCEL);
                                            checkForNotificationSettings(Constants.JourneyMode.COMPLETE)
                                            trackLocation(Constants.JourneyMode.COMPLETE)
                                        }
                                    }

                                    override fun OnStartJourney() {
                                        bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.NOTIFY).isVisible = true
                                        bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT).isVisible = true
                                        bnv.menu.findItem(Constants.DriverDashboardBottomNavTab.STUDENT).isVisible = true
                                        // SetAlarm(Constants.Alarm.START);
                                        checkForNotificationSettings(Constants.JourneyMode.START)
                                        trackLocation(Constants.JourneyMode.START)
                                    }
                                }
                        )
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    Constants.DriverDashboardBottomNavTab.NOTIFY -> {
                        fragment = DriverDashboardNotifyFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    Constants.DriverDashboardBottomNavTab.LOGOUT -> Logout(userModel, schoolDetailModel.schoolKey)
                    Constants.DriverDashboardBottomNavTab.EMERGENCY_CONTACT -> {
                        fragment = DriverDashboardEmergencyFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                    Constants.DriverDashboardBottomNavTab.STUDENT -> {
                        fragment = DriverDashboardStudentFragment()
                        ft.replace(R.id.fragment_container, fragment, role)
                        ft.commit()
                    }
                }
            }
        }
    }

    private fun checkForNotificationSettings(mode: String) {
        val settingService = DataRepo(AppSettingService::class.java, context).getService()
        settingService.GetNotificationSettings(userModel.SchoolId).enqueue(object : Callback<SettingBean?> {
            override fun onResponse(call: Call<SettingBean?>, response: Response<SettingBean?>) {

                val bean = response.body()
                bean?.let {
                    val model = bean.data.notifications
                    if (mode == Constants.JourneyMode.START) {
                        if (model.notifyOnStartJourney)
                            notifyOnStartJourney()

                    } else {
                        if (model.notifyOnCompleteJourney)
                            notifyOnCompleteJourney()
                    }
                }
            }

            override fun onFailure(call: Call<SettingBean?>, t: Throwable) {}
        })
    }

    private fun notifyOnStartJourney() {
        var msg = ""
        val detailModel = userPref.journeyDetail
        msg = if (detailModel.pickup) {
            "Dear parent, Bus - " + detailModel.vehicleNumber + "(Route - " + detailModel.routeName + ") is on the way to pick your ward. \n" +
                    "Journey started at " + detailModel.startTime
        } else {
            "Dear parent, Bus - " + detailModel.vehicleNumber + "(Route - " + detailModel.routeName + ") is on the way to drop your ward. \n" +
                    "Please reach at your assigned stoppage"
        }
        if (fragment !is DriverDashboardNotifyFragment) {
            fragment = DriverDashboardNotifyFragment()
        }
        (fragment as DriverDashboardNotifyFragment).SendNotification("", msg, context, userModel, true, application)
    }

    private fun notifyOnCompleteJourney() {
        var msg = ""
        val detailModel = userPref.journeyDetail
        msg = if (detailModel.pickup) {
            "Dear parent, your ward has reached school"
        } else { /*msg = "Dear parent, Bus - " + detailModel.getVehicleNumber() + "(Route - " + detailModel.getRouteName() + ") has dropped all students at their stoppage.";*/
            "Dear parent, all students have been dropped at their stoppage"
        }
        if (fragment !is DriverDashboardNotifyFragment) {
            fragment = DriverDashboardNotifyFragment()
        }
        (fragment as DriverDashboardNotifyFragment).SendNotification("", msg, context, userModel, true, application)
    }

    private fun showLoader(msg: String) {
        p_dialog = Utility.showProgressDialog(context, msg)
    }

    private fun hideLoader() {
        p_dialog!!.dismiss()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else
            finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return when (id) {
            android.R.id.home -> true
            R.id.action_settings -> true
            R.id.notification ->  //  RelativeLayout notificationRow = item.getActionView().findViewById(R.id.notificationRow);
                false
            else -> false
        }
    }

    private fun Logout(userModel: UserModel?, schoolKey: String) {
        val pref = UserPreference.getInstance(context)
        LoginActivity().SaveLogSignInOut(userModel, schoolKey, Constants.LogType.LOGOUT, pref.deviceToken)
        unSubscribeFromTopic(pref.GetSubscriptionTopic())
        pref.logOut()
        val loginIntent = Intent(context, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { // Handle navigation view item clicks here.
        val id = item.itemId
        when (id) {
            R.id.nav_logout -> when (role) {
                Constants.Role.DRIVER -> logoutDriver()
                else -> {
                    Logout(userModel, schoolDetailModel.schoolKey)
                    Toast.makeText(context, "Successfully signed out.", Toast.LENGTH_LONG).show()
                }
            }
            R.id.check_app_update -> checkForUpdate(context, false)
            R.id.nav_help -> {
                var url: String? = null
                when (role) {
                    Constants.Role.ADMIN, Constants.Role.TEACHER -> url = Constants.ADMIN_TEACHER_HELP_URL
                    Constants.Role.PARENT -> url = Constants.PARENT_HELP_URL
                }
                if (url != null) {
                    openInAppBrowser(url, schoolDetailModel.schoolName, "Help", R.string.helpUrlError)
                }
            }
            R.id.nav_earn -> openInAppBrowser(Constants.SHARE_EARN_URL, schoolDetailModel.schoolName, "Share and earn", R.string.urlError)
            R.id.nav_share -> Utility.ShareAppLink(context)
            R.id.nav_rating -> Utility.OpenAppInPlayStore(context)
            R.id.nav_terms -> openInAppBrowser(Constants.TERMS_CONDITIONS_URL, schoolDetailModel.schoolName, "Terms and conditions", R.string.urlError)
            R.id.nav_about_us -> openInAppBrowser(Constants.ABOUT_US_URL, schoolDetailModel.schoolName, "About us", R.string.urlError)
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun openInAppBrowser(url: String?, title: String?, subtitle: String?, errorMsg: Int) {
        Utility.openInAppBrowser(context, url, title, subtitle, errorMsg)

        /*  if (!url.isNullOrEmpty()) {
              val helpIntent = Intent(context, HelpActivity::class.java)
              helpIntent.putExtra("webUrl", url)

              helpIntent.putExtra("title", title ?: "")
              helpIntent.putExtra("subtitle", subtitle ?: "")
              startActivity(helpIntent)
          } else Toast.makeText(context, getString(errorMsg), Toast.LENGTH_SHORT).show()*/
    }

    override fun openLinkInSystemBrowser(url: String?, errorMsg: Int) {
        Utility.openLinkInSystemBrowser(url, errorMsg, context)
        /* if (!url.isNullOrEmpty()) {
             try {
                 startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
             } catch (ex: ActivityNotFoundException) {
                 Toast.makeText(context, R.string.unexpectedErrMsg, Toast.LENGTH_SHORT).show()
             }

         } else Toast.makeText(context, getString(errorMsg), Toast.LENGTH_SHORT).show()*/
    }

    override fun onUpdateNeeded(updateUrl: String) {
        try {
            Utility.ShowUpdateAppWindow(context)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun checkForUpdate(context: Context?, isAutomatic: Boolean) {
        try {
            if (!isAutomatic)
                showLoader("Checking for update...")

            val versionCode = BuildConfig.VERSION_CODE
            val versionName = BuildConfig.VERSION_NAME
            val stage = Constants.BUILD_TYPE

            val service: LoginService = DataRepo(LoginService::class.java, this@MainActivity).getService()
            service.checkAppVersion(stage).enqueue(object : Callback<CheckAppVersionBean?> {
                override fun onResponse(call: Call<CheckAppVersionBean?>, response: Response<CheckAppVersionBean?>) {
                    val resp = response.body()

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {
                            val versionModel = resp.data
                            if (versionModel.VersionNumber > versionCode)
                                Utility.ShowUpdateAppWindow(context)
                            else {
                                if (!isAutomatic)
                                    Toast.makeText(context, "No updates available!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (p_dialog != null)
                        hideLoader()
                }

                override fun onFailure(call: Call<CheckAppVersionBean?>, t: Throwable) {
                    if (!isAutomatic)
                        Toast.makeText(context, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show()

                    if (p_dialog != null)
                        hideLoader()
                }
            })
        } catch (ex: Exception) {
            Log.e("Check for update", ex.message)
        }
    }

    /*Fused location api*/
    protected fun trackLocation(journeyMode: String?) { // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionsToRequest = permissionsToRequest(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0)
                requestPermissions(permissionsToRequest!!.toTypedArray(), ALL_PERMISSIONS_RESULT)
        }

        when (journeyMode) {
            Constants.JourneyMode.START -> StartLocationService()
            Constants.JourneyMode.COMPLETE -> StopLocationService()
        }
    }

    private fun permissionsToRequest(wantedPermissions: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (perm in wantedPermissions) {
            if (!hasPermission(perm))
                result.add(perm)
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        if (role != null && role == Constants.Role.DRIVER)
            unregisterReceiver(providerChangeReceiver)

        if (mFusedLocationClient != null)
            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perm in permissionsToRequest!!) {
                    if (!hasPermission(perm))
                        permissionsRejected.add(perm)

                }

                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            AlertDialog.Builder(this@MainActivity).setMessage("These permissions are mandatory to get your location. You need to allow them.").setPositiveButton("OK") { dialogInterface, i ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsRejected.toTypedArray(), ALL_PERMISSIONS_RESULT)
                                }
                            }.setNegativeButton("Cancel",
                                    null).create().show()
                            return
                        }
                    }
                } else googleApiClient?.connect()
            }
        }
    }

    /*Fused location api*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GPS_REQUEST -> {
                val journeyId = userPref.journeyDetail.journeyId

                when (resultCode) {
                    Activity.RESULT_OK -> if (journeyId != 0) { // TrackLocationTask.getInstance(context).TrackLocation(journeyId, Constants.JourneyMode.START, userModel.getSchoolId());
                        StartLocationService()
                    }
                    Activity.RESULT_CANCELED -> if (journeyId != 0) {
                        checkGpsStatus()
                    }
                }
            }
        }
    }

    private fun StartLocationService() {
        val locationIntent = Intent(context, TrackLocationTask::class.java)
        locationIntent.putExtra("journeyId", userPref.journeyDetail.journeyId)
        locationIntent.putExtra("schoolId", userModel.SchoolId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(locationIntent)
        } else {
            startService(locationIntent)
        }
    }

    private fun StopLocationService() {
        val locationIntent = Intent(context, TrackLocationTask::class.java)
        stopService(locationIntent)
    }

    override fun getPermittedModules(): ArrayList<Int> {
        return mPermittedModules!!
    }

    override fun setPermittedModules(permittedModules: ArrayList<Int>) {
        this.mPermittedModules = permittedModules
    }

    override fun redirectToEnterKey() {
        val schoolKey: String
        var isPaidApp = false
        when (BuildConfig.FLAVOR) {
            Constants.ProductFlavors.LITTLE_ONE_JAIPURIA_PRESCHOOL -> {
                schoolKey = Constants.SchoolCodes.LITTLE_ONE_JAIPURIA_PRESCHOOL
                isPaidApp = true
            }
           /* Constants.ProductFlavors.JH_PODDAR_BH -> {
                schoolKey = Constants.SchoolCodes.JH_PODDAR_BH
                isPaidApp = true
            }*/
            Constants.ProductFlavors.HIM_INTERNATIONAL_SCHOOL -> {
                schoolKey = Constants.SchoolCodes.HIM_INTERNATIONAL_SCHOOL
                isPaidApp = true
            }
            else -> schoolKey = ""
        }
        val intent = Intent(this, EnterSchoolKeyActivity::class.java)
        intent.putExtra("schoolKey", schoolKey)
        intent.putExtra("isPaidApp", isPaidApp)
        startActivity(intent)
    }

    override fun redirectToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    override fun setReminder(eventId: Int, eventEndDate: String, eventType: String) { //show cal to ask date, note. And call api to save.
        getReminderInputFromUser(eventId, eventEndDate, eventType)
    }

    /**
     * Call api to save reminder
     *
     * @param eventId
     * @param note
     * @param reminderDate
     * @param eventType
     */
    private fun saveReminder(eventId: Int, note: String, reminderDate: String?, eventType: String) {
        userModel.setIsLoading(true)
        val appSettingService = DataRepo(AppSettingService::class.java, context).getService()
        val reminderRequest = ReminderRequest()
        reminderRequest.setDate(reminderDate)
        reminderRequest.setEventId(eventId)
        reminderRequest.setTask(note)
        reminderRequest.setUserId(userModel.UserId!!)
        reminderRequest.setType(eventType)
        appSettingService.SaveReminder(reminderRequest).enqueue(object : Callback<Bean?> {
            override fun onResponse(call: Call<Bean?>, response: Response<Bean?>) {
                if (response.body() != null) {
                    val bean = response.body()
                    if (bean!!.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                }
                userModel.setIsLoading(false)
            }

            override fun onFailure(call: Call<Bean?>, t: Throwable) {
                userModel.setIsLoading(false)
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Show popup to get reminder date and msg from user
     *
     * @param eventId
     * @param eventEndDate
     * @param eventType
     */
    private fun getReminderInputFromUser(eventId: Int, eventEndDate: String, eventType: String) {
        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.create()
        alertDialog.setTitle(getString(R.string.setReminder))

        val alertView = layoutInflater.inflate(R.layout.custom_reminder_alert, null, false)
        val alertRow: ConstraintLayout = alertView.findViewById(R.id.alertContainer)
        val positiveBtn = alertRow.findViewById<Button>(R.id.positiveBtn)
        val negativeBtn = alertRow.findViewById<Button>(R.id.negativeBtn)
        val reminderDateEd: TextInputEditText = alertRow.findViewById(R.id.reminderDateEd)
        val reminderNote: TextInputEditText = alertRow.findViewById(R.id.reminderNoteEd)

        reminderDateEd.setOnClickListener {
            try {
                showCalenderForReminder(reminderDateEd, eventEndDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        negativeBtn.visibility = View.VISIBLE
        positiveBtn.text = resources.getString(R.string.dialogPositiveButtonOk)
        negativeBtn.text = resources.getString(R.string.dialogNegativeButtonCancel)
        positiveBtn.setOnClickListener {
            val note = reminderNote.text.toString()
            saveReminder(eventId, note, reminderDate, eventType)
            alertDialog.dismiss()
        }
        negativeBtn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.setView(alertView)
        alertDialog.show()
    }

    /**
     * Show Calender.
     *
     * @param reminderDateEd
     * @param reminderEndDate
     * @throws ParseException
     */
    @Throws(ParseException::class)
    private fun showCalenderForReminder(reminderDateEd: TextInputEditText, reminderEndDate: String) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[year, month] = dayOfMonth
            val sdfMDY = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            reminderDate = sdfMDY.format(calendar.time)
            val sdfDMY = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            reminderDateEd.setText(sdfDMY.format(calendar.time))
        },
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.datePicker.minDate = Date().time
        //        if (!reminderEndDate.equals("")) {
//            Date endDateObj = new SimpleDateFormat("", Locale.getDefault()).parse(reminderEndDate);
//            datePickerDialog.getDatePicker().setMaxDate(endDateObj.getTime());
//        }
        datePickerDialog.show()
    }

    override fun checkIn(eventId: Int, childAdapterPosition: Int, parentAdapterPosition: Int) { //Call check in api.
        val appSettingService = DataRepo(AppSettingService::class.java, context).getService()
        val reminderRequest = ReminderRequest()
        reminderRequest.setEventId(eventId)
        reminderRequest.setUserId(userModel.UserId)
        appSettingService.SaveCheckIn(reminderRequest).enqueue(object : Callback<Bean?> {
            override fun onResponse(call: Call<Bean?>, response: Response<Bean?>) {
                if (response.body() != null) {
                    val bean = response.body()
                    if (bean!!.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, R.string.successfullyCheckedIn, Toast.LENGTH_SHORT).show()
                        updateCheckInStatus(childAdapterPosition, parentAdapterPosition)
                    } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Bean?>, t: Throwable) {
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Disable Check in and RSVP buttons after checked in.
     *
     * @param childAdapterPosition
     * @param parentAdapterPosition
     */
    private fun updateCheckInStatus(childAdapterPosition: Int, parentAdapterPosition: Int) {
        val fragment = supportFragmentManager.findFragmentByTag(userModel.RoleTitle)
        if (fragment != null) {
            when (userModel.RoleTitle) {
                Constants.Role.ADMIN -> {
                    val adminFragment = fragment as AdminDashboardHomeFragment
                    if (adminFragment.isVisible) adminFragment.updateCheckInStatus(parentAdapterPosition, childAdapterPosition)
                }
                Constants.Role.TEACHER -> if (userModel.ClassId == null || userModel.ClassId == -1) {
                    val stFrag = fragment as SubjectTeacherDashboardHomeFragment
                    if (stFrag.isVisible) stFrag.updateCheckInStatus(parentAdapterPosition, childAdapterPosition)
                } else {
                    val homeFragment = fragment as ClassTeacherDashboardHomeFragment
                    if (homeFragment.isVisible) homeFragment.updateCheckInStatus(parentAdapterPosition, childAdapterPosition)
                }
                Constants.Role.PARENT -> {
                    val homeFragment = fragment as ParentDashboardHomeFragment
                    if (homeFragment.isVisible) homeFragment.updateCheckInStatus(parentAdapterPosition, childAdapterPosition)
                }
            }
        }
    }

    override fun rsvp(eventId: Int, rsvp: String) {
        val appSettingService = DataRepo(AppSettingService::class.java, context).getService()
        val reminderRequest = ReminderRequest()
        reminderRequest.setEventId(eventId)
        reminderRequest.setUserId(userModel.UserId)
        if (rsvp == getString(R.string.yesAttending)) {
            reminderRequest.setAreYouInterested(1)
        } else if (rsvp == getString(R.string.notAttending)) {
            reminderRequest.setAreYouInterested(2)
        } else if (rsvp == getString(R.string.notSure)) {
            reminderRequest.setAreYouInterested(3)
        }
        appSettingService.RSVP(reminderRequest).enqueue(object : Callback<Bean?> {
            override fun onResponse(call: Call<Bean?>, response: Response<Bean?>) {
                if (response.body() != null) {
                    val bean = response.body()
                    if (bean!!.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, R.string.respondedSuccessfully, Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Bean?>, t: Throwable) {
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun showFullDesc(desc: String, title: String) { /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();

        alertDialog.setTitle(title == null || title.equals("") ? getString(R.string.desc) : title);
        alertDialog.setMessage(desc);
        alertDialog.show();*/
        val parent = findViewById<CoordinatorLayout>(R.id.mainLayout)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.row_desc_alert, parent, false) as ScrollView

        val tvInfo = row.findViewById<TextView>(R.id.info)
        val tvDesc = row.findViewById<TextView>(R.id.desc)

        tvInfo.visibility = View.GONE
        tvDesc.text = desc

        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(if (title.isEmpty()) getString(R.string.desc) else title)
                .setView(row)
                .show()

        val positiveBtn = row.findViewById<Button>(R.id.positiveBtn)
        val negativeBtn = row.findViewById<Button>(R.id.negativeBtn)
        positiveBtn.visibility = View.GONE
        negativeBtn.setText(R.string.close)
        negativeBtn.setOnClickListener { alertDialog.dismiss() }
    }

    override fun showFullDesc(desc: String, title: String, startDate: String) {
        val parent = findViewById<CoordinatorLayout>(R.id.mainLayout)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.row_desc_alert, parent, false) as ScrollView
        val tvInfo = row.findViewById<TextView>(R.id.info)
        val tvDesc = row.findViewById<TextView>(R.id.desc)
        tvInfo.text = startDate
        tvDesc.text = desc
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(if (title.isEmpty()) getString(R.string.desc) else title)
                .setView(row)
                .show()
        val positiveBtn = row.findViewById<Button>(R.id.positiveBtn)
        val negativeBtn = row.findViewById<Button>(R.id.negativeBtn)
        positiveBtn.visibility = View.GONE
        negativeBtn.setText(R.string.close)
        negativeBtn.setOnClickListener { alertDialog.dismiss() }
    }

    override fun redirectToNotification() {
        val notificationIntent = Intent(context, NotificationsActivity::class.java)
        startActivity(notificationIntent)
    }

    override fun performSilentLogin(returnUrl: String, openLinkInWebView: Boolean) {
        getJWTToken(SilentLogin.HTTPS + schoolDetailModel.subDomain + returnUrl, openLinkInWebView)
    }

    /**
     * Call api to get JWT token
     *@param returnUrl redirect to url
     */
    private fun getJWTToken(returnUrl: String, openLinkInWebView: Boolean) {
        showLoader("Loading...")
        val service = DataRepo(LoginService::class.java, context, ApiConstants.SILENT_LOGIN_BASE_URL, ApiConstants.AUTHENTICATION_TOKEN).getService()

        service.getJWTToken(FedratedLoginInput(userModel.UserId, userModel.UserName.trim(), schoolDetailModel.SchoolKey.trim()))
                .enqueue(object : Callback<FedratedLoginBean> {
                    override fun onResponse(call: Call<FedratedLoginBean>, response: Response<FedratedLoginBean>) {

                        val bean = response.body()
                        bean?.let {
                            if (!it.token.equals("")) redirectToNext(returnUrl, it.token, openLinkInWebView)
                            //TODO set msg
                            else Utility.showAlertDialog(context, null, null, getString(R.string.silentLoginErrorMsg))
                        }
                                ?: run { Utility.showAlertDialog(context, null, null, getString(R.string.somethingWrongMsg)) }
                        hideLoader()
                    }

                    override fun onFailure(call: Call<FedratedLoginBean>, t: Throwable) {
                        Utility.showAlertDialog(context, null, null, t.message)
                        hideLoader()
                    }
                })
    }

    fun redirectToNext(returnUrl: String, token: String, openLinkInWebView: Boolean) {
        val url = SilentLogin.HTTPS + schoolDetailModel.subDomain + SilentLogin.SCIENTIFICSTUDY_IN + "sso/do?jwt=" + token + "&returnUrl=" + returnUrl

        if (openLinkInWebView)
            openInAppBrowser(url, "", "", R.string.urlError)
        else
            openLinkInSystemBrowser(url = url, errorMsg = R.string.unableToOpen)
    }

    override fun redirectToTodayTab(tab: Int) {
//        SwitchFragment(tab)
//        val tabId = Constants.AdminDashboardBottomNavTab.TODAY
        bnv.setSelectedItemId(Constants.AdminDashboardBottomNavTab.TODAY)
    }

    companion object {
        // integer for permissions results request
        private const val ALL_PERMISSIONS_RESULT = 1011
    }
}