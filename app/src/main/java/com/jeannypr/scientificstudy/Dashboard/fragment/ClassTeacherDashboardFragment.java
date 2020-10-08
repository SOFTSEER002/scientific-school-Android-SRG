package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jeannypr.scientificstudy.Attendance.activity.TakeStudentAttendanceActivity;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.navigator.CTDashboardToolsNavigator;
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator;
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity;
import com.jeannypr.scientificstudy.Classwork.activity.CwHwListActivity;
import com.jeannypr.scientificstudy.Dashboard.model.SchoolSettingBean;
import com.jeannypr.scientificstudy.Exam.activity.EnterMarkSelectClassActivity;
import com.jeannypr.scientificstudy.Holiday.activity.HolidayActivity;
import com.jeannypr.scientificstudy.Login.api.SchoolService;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity;
import com.jeannypr.scientificstudy.Syllabus.activity.SyllabusListActivity;
import com.jeannypr.scientificstudy.Teacher.activity.MyClassListActivity;
import com.jeannypr.scientificstudy.Teacher.activity.SelfAttendanceActivity;
import com.jeannypr.scientificstudy.Teacher.activity.TeacherListActivity;
import com.jeannypr.scientificstudy.Timetable.activity.TimetableModuleActivity;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ContentClassTeacherBinding;
import com.jeannypr.scientificstudy.leave.activity.LeaveModuleActivity;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTeacherDashboardFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private UserModel userModel;
    private SchoolService schoolService;
    private UserPreference pref;
    private Boolean isApprover, isSelfAttendanceEnabled;
    private ContentClassTeacherBinding mViewBinding;
    private CTDashboardToolsNavigator mNavigator;

    /*public ClassTeacherDashboardFragment(CTDashboardToolsNavigator mNavigator) {
        this.mNavigator = mNavigator;
    }*/
    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        mNavigator = (CTDashboardToolsNavigator) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNavigator = (CTDashboardToolsNavigator) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public ClassTeacherDashboardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        setHasOptionsMenu(true);
        pref = UserPreference.getInstance(context);
        userModel = pref.getUserData();
        schoolService = new DataRepo<>(SchoolService.class, context).getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.content_class_teacher, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isSelfAttendanceEnabled = false;

        mViewBinding.sptTeacherStaffList.setOnClickListener(this);
        mViewBinding.myClassAttendanceModule.setOnClickListener(this);
        mViewBinding.enterMarks.setOnClickListener(this);
        mViewBinding.myClassesModule.setOnClickListener(this);

        mViewBinding.teacherSelfAttendance.setOnClickListener(this);
        mViewBinding.clsTeacherClasswork.setOnClickListener(this);
        mViewBinding.clsTeacherHomework.setOnClickListener(this);
        mViewBinding.clsTeacherTimetable.setOnClickListener(this);

        mViewBinding.myLeaveCT.setOnClickListener(this);
        mViewBinding.holidayModule.setOnClickListener(this);
        mViewBinding.newsModule.setOnClickListener(this);
        mViewBinding.noticeModule.setOnClickListener(this);
        mViewBinding.eventModule.setOnClickListener(this);

        mViewBinding.notificationModule.setOnClickListener(this);
        mViewBinding.surveyModule.setOnClickListener(this);
        mViewBinding.helpdeskModule.setOnClickListener(this);
        mViewBinding.sendMsgBtn.setOnClickListener(this);
        mViewBinding.ptmBtn.setOnClickListener(this);
        mViewBinding.syllabus.setOnClickListener(this);
        mViewBinding.lessonPlan.setOnClickListener(this);
        mViewBinding.parentHDMsg.setOnClickListener(this);
        CheckSchoolSettings();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sptTeacherStaffList:
                Intent teacherListIntent = new Intent(context, TeacherListActivity.class);
                startActivity(teacherListIntent);
                break;

            case R.id.myClassAttendanceModule:
                Intent takeAttendance = new Intent(context, TakeStudentAttendanceActivity.class);
                takeAttendance.putExtra("ClassId", userModel.getClassId());
                takeAttendance.putExtra("ClassName", userModel.getClassName());
                startActivity(takeAttendance);
                break;

            case R.id.enterMarks:
                Intent examModuleIntent = new Intent(context, EnterMarkSelectClassActivity.class);
                examModuleIntent.putExtra("ClassId", userModel.getClassId());
                examModuleIntent.putExtra("ClassName", userModel.getClassName());
                startActivity(examModuleIntent);
                break;

            case R.id.myClassesModule:
                Intent classListIntent = new Intent(context, MyClassListActivity.class);
                startActivity(classListIntent);
                break;

            case R.id.teacherSelfAttendance:
                Intent selfAttendanceIntent = new Intent(context, SelfAttendanceActivity.class);
                startActivity(selfAttendanceIntent);
                break;

            case R.id.clsTeacherClasswork:
                Intent clsTeacherClassworkIntent = new Intent(context, CwHwListActivity.class);
                clsTeacherClassworkIntent.putExtra("activityType", Constants.DiaryType.Classwork);
                startActivity(clsTeacherClassworkIntent);
                break;
            case R.id.clsTeacherHomework:
                Intent adminHomeworkIntent = new Intent(context, CwHwListActivity.class);
                adminHomeworkIntent.putExtra("activityType", Constants.DiaryType.Homework);
                startActivity(adminHomeworkIntent);
                break;

            case R.id.clsTeacherTimetable:
                Intent i = new Intent(context, TimetableModuleActivity.class);
                i.putExtra("timetableOf", Constants.TimetableOf.STAFF);
                startActivity(i);
                break;

            case R.id.myLeaveCT:
                Intent leaveIntent = new Intent(context, LeaveModuleActivity.class);
                leaveIntent.putExtra("isApprover", false);
                startActivity(leaveIntent);
                break;

            case R.id.holidayModule:
                Intent holidayIntent = new Intent(context, HolidayActivity.class);
                startActivity(holidayIntent);
                break;

            case R.id.newsModule:
                Intent holidayIntent1 = new Intent(context, SptWallActivity.class).putExtra("news", Constants.PostType.NEWS);
                startActivity(holidayIntent1);
                break;

            case R.id.eventModule:
                Intent holidayIntent12 = new Intent(context, SptWallActivity.class).putExtra("news", Constants.PostType.EVENT);
                startActivity(holidayIntent12);
                break;

            case R.id.noticeModule:
                Intent holidayIntent123 = new Intent(context, SptWallActivity.class).putExtra("news", Constants.PostType.NOTICE);
                startActivity(holidayIntent123);
                break;

            case R.id.sendMsgBtn:
                redirectToChat();
                break;

            case R.id.notificationModule:
                mNavigator.redirectToNotification();
                break;

            case R.id.surveyModule:
                mNavigator.performSilentLogin(SilentLogin.SURVEY_URL, true);
                break;

            case R.id.helpdeskModule:
                mNavigator.openLinkInSystemBrowser(Constants.ADMIN_TEACHER_HELP_URL, R.string.helpUrlError);
                break;

            case R.id.ptmBtn:
                redirectToWall(Constants.PostType.EVENT, Constants.EventType.PTM);
                break;

            case R.id.syllabus:
                Intent syllabusIntent = new Intent(context, SyllabusListActivity.class);
                startActivity(syllabusIntent);
                break;

            case R.id.lessonPlan:
                mNavigator.performSilentLogin(SilentLogin.LESSON_PLAN_URL, false);
                break;

            case R.id.parentHDMsg:
                mNavigator.openInAppBrowser(userModel.getParentHelpdeskUrl(), "", "", R.string.linkNotFoundMsg);
                break;

           /* case R.id.paidFeeRow:
                redirectToFee(true);
                break;

            case R.id.dueFeeRow:
                redirectToFee(false);
                break;

            case R.id.feeBtn:
                if (isOnlineFeeAvailable)
                    payFeeOnline();
                else
                    Utility.showAlertDialog(context, null, getString(R.string.moduleNotGrantedHeader), getString(R.string.moduleNotGrantedMsg));
                break;*/
        }
    }

    private void redirectToChat() {
        Intent chatIntent = new Intent(context, ChatGroupActivity.class);
        startActivity(chatIntent);
    }

    private void redirectToWall(String postType, String eventType) {
        Intent holidayIntent12 = new Intent(context, SptWallActivity.class).putExtra("news", postType).putExtra(Constants.FILTER_BY, eventType);
        startActivity(holidayIntent12);
    }

    private void CheckSchoolSettings() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        schoolService.CheckSchoolSettings(userModel.getSchoolId()).enqueue(new Callback<SchoolSettingBean>() {
            @Override
            public void onResponse(Call<SchoolSettingBean> call, Response<SchoolSettingBean> response) {
                if (response.body() != null) {
                    SchoolSettingBean bean = response.body();

                    if (bean.data != null) {
                        isSelfAttendanceEnabled = bean.data.isSelfAttendanceEnabled();
                        if (isSelfAttendanceEnabled) {
                            mViewBinding.teacherSelfAttendance.setVisibility(View.VISIBLE);
                        } else {
                            mViewBinding.teacherSelfAttendance.setVisibility(View.GONE);
                        }

                        if (!userModel.getRoleTitle().equals(Constants.Role.ADMIN))
                            pref.getSchoolData().setCanSeeContactNumber(bean.data.getCanSeeContactNumber());
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

    @Override
    public void onResume() {
        super.onResume();
        CheckSchoolSettings();
    }
}