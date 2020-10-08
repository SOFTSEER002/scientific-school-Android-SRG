package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity;
import com.jeannypr.scientificstudy.Dashboard.adapter.AcademicModulesDashboardAdapter;
import com.jeannypr.scientificstudy.Dashboard.adapter.CommunicationModulesDashboardAdapter;
import com.jeannypr.scientificstudy.Dashboard.adapter.ManagementModulesDashboardAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesBean;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel;
import com.jeannypr.scientificstudy.Dashboard.model.SchoolSettingBean;
import com.jeannypr.scientificstudy.Login.api.SchoolService;
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity;
import com.jeannypr.scientificstudy.Timetable.activity.StaffTimetableModuleActivity;
import com.jeannypr.scientificstudy.Timetable.activity.StudentTimetableModuleActivity;
import com.jeannypr.scientificstudy.Timetable.activity.TimetableModuleActivity;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ContentMain2Binding;
import com.jeannypr.scientificstudy.leave.api.LeaveService;
import com.jeannypr.scientificstudy.leave.model.IsApproverBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardFragment extends Fragment implements
        View.OnClickListener,
        ManagementModulesDashboardAdapter.DashBoardModulesAdapterNavigator,
        CommunicationModulesDashboardAdapter.DashBoardModulesAdapterNavigator,
        AcademicModulesDashboardAdapter.DashBoardModulesAdapterNavigator {

    private Context context;
    private UserPreference userPref;
    private UserModel userModel;
    private LeaveService leaveService;
    private SchoolService schoolService;
    private Boolean isApprover, isSelfAttendanceEnabled;
    private MenuItem itemCart;
    private TextView notificationTxt;
    private ManagementModulesDashboardAdapter mManagementAdapter;
    private AcademicModulesDashboardAdapter mAcademicAdapter;
    private CommunicationModulesDashboardAdapter mCommunicationAdapter;
    private ArrayList<DashboardModulesModel> mCommunicationTools, mAcademicTools, mManagementTools;
    private AdminDashboardNavigator adminDashboardNavigator;
    private ContentMain2Binding mViewBinding;
    private DashboardModulesModel myTimetable, staffTimetable, studentTimetable, misReports;

    public AdminDashboardFragment() {
    }

    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AdminDashboardNavigator) {
            adminDashboardNavigator = (AdminDashboardNavigator) activity;
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminDashboardNavigator) {
            adminDashboardNavigator = (AdminDashboardNavigator) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
        leaveService = new DataRepo<>(LeaveService.class, context).getService();
        schoolService = new DataRepo<>(SchoolService.class, context).getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.content_main2, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        isSelfAttendanceEnabled = false;

        setClickListners();
        if (Utility.isConnectedToInternet(context)) {
            IsApprover();
            CheckSchoolSettings();
            GetAllModules();
        } else displayErrorMsg(R.string.noInternetMsg);

        initializeAdapters();
    }

    private void setClickListners() {
        mViewBinding.sendMsgBtn.setOnClickListener(this);
        mViewBinding.ptmBtn.setOnClickListener(this);
        mViewBinding.myTimetableBtn.setOnClickListener(this);
        mViewBinding.staffTimetableRow.setOnClickListener(this);
        mViewBinding.studentTimetableRow.setOnClickListener(this);
        mViewBinding.misRow.setOnClickListener(this);
//        mViewBinding.parentHDMsg.setOnClickListener(this);

        mViewBinding.shareArticlesRow.setOnClickListener(this);
        mViewBinding.shareHelpdeskRow.setOnClickListener(this);
        mViewBinding.parentOrientationkRow.setOnClickListener(this);
        mViewBinding.staffOrientationRow.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        IsApprover();
        CheckSchoolSettings();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        itemCart = menu.findItem(R.id.notification);
        View actionView = itemCart.getActionView();
        try {
            if (actionView != null) {
                notificationTxt = actionView.findViewById(R.id.cartBadge);
                notificationTxt.setText(userPref.GetUnreadMessages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreateOptionsMenu(menu, inflater);
        RelativeLayout notificationRow = (RelativeLayout) menu.findItem(R.id.notification).getActionView();
        notificationRow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.notificationRow:
                userPref.SetTotalUnreadMessages(0);
                notificationTxt.setText("");
                Intent notificationIntent = new Intent(context, NotificationsActivity.class);
                startActivity(notificationIntent);
                break;

            case R.id.sendMsgBtn:
                redirectToChat();
                break;

            case R.id.ptmBtn:
                redirectToWall(Constants.PostType.EVENT, Constants.EventType.PTM);
                break;

            case R.id.myTimetableBtn:
                initTimetable(myTimetable);
                break;

            case R.id.staffTimetableRow:
                initTimetable(staffTimetable);
                break;

            case R.id.studentTimetableRow:
                initTimetable(studentTimetable);
                break;

            case R.id.misRow:
                initTimetable(misReports);
                break;

      /*      case R.id.parentHDMsg:
                adminDashboardNavigator.openInAppBrowser(userModel.getParentHelpdeskUrl(), "", "", R.string.linkNotFoundMsg);
                break;*/

            case R.id.shareArticlesRow:
                Utility.openLinkInSystemBrowser(Constants.SHARE_ARTICLES_URL, R.string.linkNotFoundMsg, context);
                break;

            case R.id.shareHelpdeskRow:
                Utility.openLinkInSystemBrowser(userModel.getParentHelpdeskUrl(), R.string.linkNotFoundMsg, context);
                break;

            case R.id.parentOrientationkRow:
                Utility.openLinkInSystemBrowser(Constants.PARENTS_ORIENTATION_URL, R.string.linkNotFoundMsg, context);
                break;

            case R.id.staffOrientationRow:
                Utility.openLinkInSystemBrowser(Constants.STAFF_ORIENTATION_URL, R.string.linkNotFoundMsg, context);
                break;
        }
    }

    /**
     * Initialize adapters of all categories.
     */
    private void initializeAdapters() {
        mManagementTools = new ArrayList<>();
        mManagementAdapter = new ManagementModulesDashboardAdapter(context, mManagementTools, this);
        mViewBinding.managementRV.setAdapter(mManagementAdapter);

        mCommunicationTools = new ArrayList<>();
        mCommunicationAdapter = new CommunicationModulesDashboardAdapter(context, mCommunicationTools, this);
        mViewBinding.communicationRV.setAdapter(mCommunicationAdapter);

        mAcademicTools = new ArrayList<>();
        mAcademicAdapter = new AcademicModulesDashboardAdapter(context, mAcademicTools, this);
        mViewBinding.academicRV.setAdapter(mAcademicAdapter);

        mViewBinding.managementRV.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.totalColumn), GridLayoutManager.VERTICAL, false));
        mViewBinding.communicationRV.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.totalColumn), GridLayoutManager.VERTICAL, false));
        mViewBinding.academicRV.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.totalColumn), GridLayoutManager.VERTICAL, false));
    }

    /**
     * Check if permission for module is granted
     * if yes , redirect to corresponding module
     * if not, show alert message
     *
     * @param menu
     */
    private void initTimetable(DashboardModulesModel menu) {
        Boolean isGranted = isModuleGranted(menu.getModuleId());

        switch (menu.getModuleName()) {
            case Constants.AdminModules.STAFF_TIMETABLE_MODULE:
                if (isGranted)
                    redirectToStaffTimetable();
                else
                    Utility.showAlertDialog(context, null, getString(R.string.moduleNotGrantedHeader), getString(R.string.moduleNotGrantedMsg));
                break;

            case Constants.AdminModules.STUDENT_TIMETABLE_MODULE:
                if (isGranted)
                    redirectToStudentTimetable();
                else
                    Utility.showAlertDialog(context, null, getString(R.string.moduleNotGrantedHeader), getString(R.string.moduleNotGrantedMsg));
                break;

            case Constants.AdminModules.MY_TIMETABLE_MODULE:
                if (isGranted)
                    redirectToMyTimetable();
                else
                    Utility.showAlertDialog(context, null, getString(R.string.moduleNotGrantedHeader), getString(R.string.moduleNotGrantedMsg));
                break;

            case Constants.AdminModules.MIS_REPORTS:
                if (isGranted)
                    performSilentLogin(SilentLogin.MIS_REPORT_URL, true);
                else
                    Utility.showAlertDialog(context, null, getString(R.string.moduleNotGrantedHeader), getString(R.string.moduleNotGrantedMsg));
                break;
        }


    }

    private void displayErrorMsg(int errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
    }

    private void IsApprover() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        leaveService.IsApprover(userModel.getSchoolId(), userModel.getUserId()).enqueue(new Callback<IsApproverBean>() {
            @Override
            public void onResponse(Call<IsApproverBean> call, Response<IsApproverBean> response) {

                if (response.body() != null) {
                    isApprover = response.body().data.IsApprover;

                  /*  if (isApprover) {
                        adminLeave.setVisibility(View.VISIBLE);

                        adminLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent leaveIntent = new Intent(context, LeaveModuleActivity.class);
                                leaveIntent.putExtra("isApprover", true);
                                startActivity(leaveIntent);
                            }
                        });
                    }*/
                }
                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<IsApproverBean> call, Throwable t) {
                Log.e("IsApprover call error :", t.getMessage());
                mViewBinding.pb.setVisibility(View.GONE);
            }
        });

    }

    private void CheckSchoolSettings() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        schoolService.CheckSchoolSettings(userModel.getSchoolId()).enqueue(new Callback<SchoolSettingBean>() {
            @Override
            public void onResponse(Call<SchoolSettingBean> call, Response<SchoolSettingBean> response) {
                if (response.body() != null) {
                    SchoolSettingBean bean = response.body();

                    try {
                        if (bean != null && bean.data != null) {
                            isSelfAttendanceEnabled = bean.data.isSelfAttendanceEnabled();
                            //TODO: set visibility at run time
                        }
                    } catch (Exception ex) {
                        Log.e("School setting", ex.getMessage());
                    }
                }

                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SchoolSettingBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void GetAllModules() {
        mViewBinding.pb.setVisibility(View.VISIBLE);
        AppSettingService service = new DataRepo<>(AppSettingService.class, getActivity()).getService();

        service.GetAllModulesList(userModel.getRoleTitle()).enqueue(new Callback<DashboardModulesBean>() {
            @Override
            public void onResponse(Call<DashboardModulesBean> call, Response<DashboardModulesBean> response) {
                if (response != null) {
                    if (response.body() != null) {

                        DashboardModulesBean bean = response.body();
                        if (bean.data != null) {

                            //TODO: bind data to adapter
                            for (DashboardModulesModel datum : bean.data) {
                                switch (datum.getModuleName()) {
                                    //TODO: admin modules - Syllabus, notification, survey, helpdesk, transport. - add in list, set listners

                                    case Constants.AdminModules.REGISTRATION_MODULE:
                                    case Constants.AdminModules.FEE_MODULE:
                                    case Constants.AdminModules.INVENTORY_MODULE:
                                    case Constants.AdminModules.ALLOW_DISCOUNT:
                                    case Constants.AdminModules.STAFF_MODULE:
                                    case Constants.AdminModules.STUDENT_MODULE:
                                    case Constants.AdminModules.TRANSPORT_MODULE:
                                    case Constants.AdminModules.ACCOUNTS_MODULE:
                                    case Constants.AdminModules.LEAVE_MODULE:
                                    case Constants.AdminModules.MY_LEAVE_MODULE:
                                        mManagementTools.add(datum);
                                        break;

                                    case Constants.AdminModules.CW_MODULE:
                                    case Constants.AdminModules.HW_MODULE:
                                    case Constants.AdminModules.EXAM_MODULE:
                                    case Constants.AdminModules.ATTENDANCE_MODULE:
                                    case Constants.AdminModules.SELF_ATTENDANCE_MODULE:
                                    case Constants.AdminModules.SYLLABUS:
                                    case Constants.AdminModules.LESSON_PLAN:
                                        mAcademicTools.add(datum);
                                        break;

                                    case Constants.AdminModules.H_NEWS:
                                    case Constants.AdminModules.H_NOTICE:
                                    case Constants.AdminModules.H_EVENT:
                                    case Constants.AdminModules.H_HOLIDAYS:
                                    case Constants.AdminModules.SURVEY:
                                    case Constants.AdminModules.HELPDESK:
                                    case Constants.AdminModules.NOTIFICATION:
                                        mCommunicationTools.add(datum);
                                        break;

                                    case Constants.AdminModules.MY_TIMETABLE_MODULE:
                                        myTimetable = datum;
                                        break;
                                    case Constants.AdminModules.STAFF_TIMETABLE_MODULE:
                                        staffTimetable = datum;
                                        break;
                                    case Constants.AdminModules.STUDENT_TIMETABLE_MODULE:
                                        studentTimetable = datum;
                                        break;

                                    case Constants.AdminModules.MIS_REPORTS:
                                        misReports = datum;
                                        break;
                                }

                            }
                            mManagementAdapter.notifyDataSetChanged();
                            mCommunicationAdapter.notifyDataSetChanged();
                            mAcademicAdapter.notifyDataSetChanged();
                        }
                    }
                }
                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DashboardModulesBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.technicalError), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * redirect to timetable module
     */
    private void redirectToStaffTimetable() {
        Intent intent = new Intent(context, StaffTimetableModuleActivity.class).putExtra(Constants.Timetable, Constants.TimetableOf.STAFF);
        startActivity(intent);
    }

    /**
     * redirect to timetable module
     */
    private void redirectToStudentTimetable() {
        Intent intent = new Intent(context, StudentTimetableModuleActivity.class).putExtra(Constants.Timetable, Constants.TimetableOf.STUDENT);
        startActivity(intent);
    }

    /**
     * redirect to timetable module
     */
    private void redirectToMyTimetable() {
        Intent intent = new Intent(context, TimetableModuleActivity.class).putExtra(Constants.Timetable, Constants.TimetableOf.SELF);
        startActivity(intent);
    }

    private ArrayList<Integer> getPermittedModules() {
        if (adminDashboardNavigator != null)
            return adminDashboardNavigator.getPermittedModules();
        return null;
    }

    @Override
    public Boolean isModuleGranted(int selectedModuleId) {
        ArrayList<Integer> permittedModules = getPermittedModules();

        if (permittedModules != null && permittedModules.size() > 0) {
            for (Integer permittedModuleId : permittedModules) {
                if (permittedModuleId == selectedModuleId) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Boolean isApprover() {
        return isApprover;
    }

    private void redirectToChat() {
        Intent chatIntent = new Intent(context, ChatGroupActivity.class);
        startActivity(chatIntent);
    }

    private void redirectToWall(String postType, String eventType) {
        Intent holidayIntent12 = new Intent(context, SptWallActivity.class).putExtra("news", postType).putExtra(Constants.FILTER_BY, eventType);
        startActivity(holidayIntent12);
    }

    @Override
    public void openBrowserInApp(String url, String title, String subtitle, int errorMsg) {
        adminDashboardNavigator.openInAppBrowser(url, title, subtitle, errorMsg);
    }

    @Override
    public void openLinkInSystemBrowser(String url, int errorMsg) {
        adminDashboardNavigator.openLinkInSystemBrowser(url, errorMsg);
    }

    @Override
    public void performSilentLogin(String returnUrl, Boolean openLinkInWebView) {
        adminDashboardNavigator.performSilentLogin(returnUrl, openLinkInWebView);
    }

    public interface AdminDashboardNavigator {
        ArrayList<Integer> getPermittedModules();

        /**
         * Override to navigate to web browser.
         *
         * @param url
         * @param title
         * @param subtitle
         */
        void openInAppBrowser(String url, String title, String subtitle, int errorMsg);

        /**
         * Override to navigate to web browser.
         *
         * @param url
         */
        void openLinkInSystemBrowser(String url, int errorMsg);

        void performSilentLogin(String returnUrl, Boolean openLinkInWebView);
    }
}