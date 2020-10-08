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

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.navigator.MainNavigator;
import com.jeannypr.scientificstudy.Base.navigator.ParentDashboardToolsNavigator;
import com.jeannypr.scientificstudy.Chat.activity.ChatGroupActivity;
import com.jeannypr.scientificstudy.Chat.activity.HelpActivity;
import com.jeannypr.scientificstudy.Classwork.activity.CwHwListActivity;
import com.jeannypr.scientificstudy.Dashboard.adapter.SliderAdapter;
import com.jeannypr.scientificstudy.Dashboard.model.SchoolSettingBean;
import com.jeannypr.scientificstudy.Exam.activity.ExamListActivity;
import com.jeannypr.scientificstudy.Fee.activity.FeeSummaryActivity;
import com.jeannypr.scientificstudy.Holiday.activity.HolidayActivity;
import com.jeannypr.scientificstudy.Login.api.SchoolService;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity;
import com.jeannypr.scientificstudy.Student.FeedbackActivity;
import com.jeannypr.scientificstudy.Student.activity.ClassTeachersActivity;
import com.jeannypr.scientificstudy.Student.activity.MonthwiseAttendanceSummaryActivity;
import com.jeannypr.scientificstudy.Student.activity.StudentLeaveHistoryActivity;
import com.jeannypr.scientificstudy.Syllabus.activity.SyllabusListActivity;
import com.jeannypr.scientificstudy.Timetable.activity.TimetableModuleActivity;
import com.jeannypr.scientificstudy.Transport.activity.TransportActivity;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ContentParentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentDashboardFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private UserPreference userPref;
    private List<ChildModel> children;
    private ChildModel selectedChild;
    private UserModel userModel;
    private LayoutInflater inflater;
    private SliderAdapter adapter;
    private SchoolDetailModel schoolData;
    private MenuItem itemCart;
    private TextView notificationTxt;
    private SchoolService schoolService;
    private Boolean isOnlineFeeAvailable;
    private ParentDashboardToolsNavigator mNavigator;
    private ContentParentBinding mViewBinding;

    /*   public ParentDashboardFragment(ParentDashboardToolsNavigator mNavigator) {
           this.mNavigator = mNavigator;
       }*/
    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        mNavigator = (ParentDashboardToolsNavigator) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNavigator = (ParentDashboardToolsNavigator) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public ParentDashboardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        schoolService = new DataRepo<>(SchoolService.class, context).getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.content_parent, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userPref = UserPreference.getInstance(context);
        schoolData = userPref.getSchoolData();
        inflater = LayoutInflater.from(context);

        isOnlineFeeAvailable = false;

        mViewBinding.classTeacherModule.setOnClickListener(this);
        mViewBinding.holidayModule.setOnClickListener(this);
        mViewBinding.myChildAttendanceModule.setOnClickListener(this);
        mViewBinding.myChildClasswork.setOnClickListener(this);
        mViewBinding.myChildHomework.setOnClickListener(this);

        mViewBinding.myChildTimetable.setOnClickListener(this);
        mViewBinding.myChildExam.setOnClickListener(this);
        mViewBinding.myChildLeaves.setOnClickListener(this);
        mViewBinding.newsModule.setOnClickListener(this);

        mViewBinding.noticeModule.setOnClickListener(this);
        mViewBinding.eventModule.setOnClickListener(this);

        mViewBinding.notificationModule.setOnClickListener(this);
        mViewBinding.surveyModule.setOnClickListener(this);
        mViewBinding.transportModule.setOnClickListener(this);
        mViewBinding.helpdeskModule.setOnClickListener(this);

        mViewBinding.sendMsgBtn.setOnClickListener(this);
        mViewBinding.ptmBtn.setOnClickListener(this);
        mViewBinding.feeBtn.setOnClickListener(this);
        mViewBinding.paidFeeRow.setOnClickListener(this);
        mViewBinding.dueFeeRow.setOnClickListener(this);
        mViewBinding.syllabus.setOnClickListener(this);
        mViewBinding.feedback.setOnClickListener(this);
        mViewBinding.onlineClassBtn.setOnClickListener(this);

        userModel = userPref.getUserData();
        children = userPref.getChildren();
        selectedChild = userPref.getSelectedChild();

        CheckSchoolSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckSchoolSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.classTeacherModule:
                Intent classTeacherIntent = new Intent(context, ClassTeachersActivity.class);
                startActivity(classTeacherIntent);
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
                redirectToWall(Constants.PostType.EVENT, "");
                break;

            case R.id.noticeModule:
                Intent holidayIntent123 = new Intent(context, SptWallActivity.class).putExtra("news", Constants.PostType.NOTICE);
                startActivity(holidayIntent123);
                break;

            case R.id.myChildAttendanceModule:
                Intent intent = new Intent(context, MonthwiseAttendanceSummaryActivity.class);
                int classId = selectedChild.ClassId;
                int studentId = selectedChild.StudentId;
                int schoolId = userModel.getSchoolId();
                int academicyearId = userModel.getAcademicyearId();

                intent.putExtra("classId", classId);
                intent.putExtra("studentId", studentId);
                intent.putExtra("schoolId", schoolId);
                intent.putExtra("academicyearId", academicyearId);
                startActivity(intent);
                break;

            case R.id.myChildClasswork:
                Intent classworkIntent = new Intent(context, CwHwListActivity.class);
                classworkIntent.putExtra("activityType", Constants.DiaryType.Classwork);
                startActivity(classworkIntent);
                break;

            case R.id.myChildHomework:
                Intent adminHomeworkIntent = new Intent(context, CwHwListActivity.class);
                adminHomeworkIntent.putExtra("activityType", Constants.DiaryType.Homework);
                startActivity(adminHomeworkIntent);
                break;

            case R.id.myChildTimetable:
                Intent timetableIntent = new Intent(context, TimetableModuleActivity.class);
                timetableIntent.putExtra("timetableOf", Constants.TimetableOf.STUDENT);
                startActivity(timetableIntent);
                break;

            case R.id.myChildExam:
                Intent examIntent = new Intent(context, ExamListActivity.class);
                startActivity(examIntent);
                break;

            case R.id.sendMsgBtn:
                redirectToChat();
                break;

            case R.id.myChildLeaves:
                Intent leaveIntent = new Intent(context, StudentLeaveHistoryActivity.class);
                startActivity(leaveIntent);
                break;

            case R.id.notificationModule:
                mNavigator.redirectToNotification();
                break;

            case R.id.surveyModule:
                mNavigator.performSilentLogin(SilentLogin.PARENT_SURVEY_URL, false);
                break;

            case R.id.syllabus:
                Intent syllabusIntent = new Intent(context, SyllabusListActivity.class);
                startActivity(syllabusIntent);
                break;

            case R.id.transportModule:
                redirectToTransport();
                break;

            case R.id.helpdeskModule:
//                mNavigator.openLinkInSystemBrowser(Constants.PARENT_HELP_URL, R.string.helpUrlError);
                if (userModel.getParentHelpdeskUrl() == null)
                    Utility.showAlertDialog(context, null, "", getString(R.string.linkNotFoundMsg));
                else
                    mNavigator.openLinkInSystemBrowser(userModel.getParentHelpdeskUrl(), R.string.helpUrlError);
                break;

            case R.id.ptmBtn:
                redirectToWall(Constants.PostType.EVENT, Constants.EventType.PTM);
                break;

            case R.id.paidFeeRow:
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
                break;
            case R.id.feedback:
                redirectToFeedback();
                break;
            case R.id.onlineClassBtn:
                mNavigator.performSilentLogin(SilentLogin.ONLINE_CLASS_URL_PARENT, false);
                break;
        }
    }

  /*  private void manageBlinkEffect() {
        ObjectAnimator anim = ObjectAnimator.ofInt(mViewBinding.feePaymentLbl, "textColor", Color.WHITE, Color.YELLOW);
        anim.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /*itemCart = menu.findItem(R.id.notification);
        View actionView = itemCart.getActionView();
        try {
            if (actionView != null) {
                notificationTxt = actionView.findViewById(R.id.cartBadge);
                notificationTxt.setText(userPref.GetUnreadMessages());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onCreateOptionsMenu(menu, inflater);
        RelativeLayout notificationRow = (RelativeLayout) menu.findItem(R.id.notification).getActionView();
        notificationRow.setOnClickListener(this);
    }

    /*@Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem i = menu.findItem(R.id.notification);
        i.getActionView();
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notification:
                Intent notification = new Intent(context, NotificationsActivity.class);
                startActivity(notification);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    private void redirectToTransport() {
        Intent transportIntent = new Intent(context, TransportActivity.class);
        transportIntent.putExtra(Constants.STUDENT_ID, selectedChild.StudentId);
        startActivity(transportIntent);
    }

    private void CheckSchoolSettings() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        schoolService.CheckSchoolSettings(userModel.getSchoolId()).enqueue(new Callback<SchoolSettingBean>() {
            @Override
            public void onResponse(Call<SchoolSettingBean> call, Response<SchoolSettingBean> response) {
                if (response.body() != null) {
                    SchoolSettingBean bean = response.body();

                    if (bean.data != null) {
                        SchoolDetailModel model = userPref.getSchoolData();
                        model.setCanParentReplyInChat(bean.data.getCanParentReplyInChat());
                        userPref.setSchoolData(model);

                        isOnlineFeeAvailable = bean.data.isOnlineFeeAvailable();

                       /* if (isOnlineFeeAvailable != null && isOnlineFeeAvailable) {
                            payFeeModule.setVisibility(View.VISIBLE);

                        } else {
                            payFeeModule.setVisibility(View.GONE);
                        }*/
                    }
                }
                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SchoolSettingBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Log.e("Online fee payment: ", t.getMessage());
                //Toast.makeText(context, "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void redirectToChat() {
        Intent chatIntent = new Intent(context, ChatGroupActivity.class);
        startActivity(chatIntent);
    }

    private void redirectToFeedback() {
        Intent intent = new Intent(context, FeedbackActivity.class);
        startActivity(intent);
    }

    private void redirectToWall(String postType, String eventType) {
        Intent holidayIntent12 = new Intent(context, SptWallActivity.class).putExtra("news", postType).putExtra(Constants.FILTER_BY, eventType);
        startActivity(holidayIntent12);
    }

    private void payFeeOnline() {
        String feeUrl = Constants.HTTPS + userPref.getSchoolData().getSubDomain() + Constants.SUBDOMAIN + "/payment?key=" + userPref.getSchoolCode() + "&mode=" + Constants.OnlinePaymentFor.ADMISSION + "&studentid=" + selectedChild.StudentId;
        mNavigator.openLinkInSystemBrowser(feeUrl, R.string.somethingWrongMsg);
    }

    private void redirectToFee(boolean isFeePaid) {
        Intent feeModuleIntent = new Intent(context, FeeSummaryActivity.class);
        feeModuleIntent.putExtra("studentName", selectedChild.Name);
        feeModuleIntent.putExtra("studentId", selectedChild.StudentId);
        feeModuleIntent.putExtra(Constants.IS_FEE_PAID, isFeePaid);
        startActivity(feeModuleIntent);
    }
}