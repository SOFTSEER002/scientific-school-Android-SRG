package com.jeannypr.scientificstudy.Classwork.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.adapter.CwHwDetailAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityInfoModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityCwHwDetailBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CwHwDetailActivity extends AppCompatActivity implements View.OnClickListener, CwHwDetailAdapter.OnItemClickListner {
    private Context context;
    private int activityId, activityTypeId;
    private String className, activityType, subjectName;
    CwHwService classworkService;
    UserPreference userPref;
    UserModel userData;
    List<ActivityItemModel> activityItemModels;
    CwHwDetailAdapter adapter;
    RecyclerView activityItems;
    ProgressBar progressBar;
    private LinearLayout noRecord;
    private TextView noRecordMsg, txtClassName, txtTitle, txtAssignedOn, txtSubmittedOn;
    ActivityInfoModel activityDetail;
    ConstraintLayout constraintLayout;
    private ActivityCwHwDetailBinding mViewBinding;
    String teacherEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_cw_hw_detail);

        classworkService = new DataRepo<>(CwHwService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();

        activityId = getIntent().getIntExtra("activityId", -1); // in case of foreground, intent will be received from myFirebaseMessaging
        // activityId = Integer.parseInt(getIntent().getStringExtra("diaryId")); // in case of background, intent will be received from server not from myFirebaseMessaging

        className = getIntent().getStringExtra("className");
        // activityType = getIntent().getStringExtra("activityType");
        subjectName = getIntent().getStringExtra("subjectName");
        activityTypeId = getIntent().getIntExtra("activityTypeId", -1);
        activityType = activityTypeId == Constants.DiaryType.Classwork ? Constants.DiaryTypeName.Classwork : Constants.DiaryTypeName.Homework;

        activityItems = findViewById(R.id.activityItems);
        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);
        progressBar = findViewById(R.id.progressBar);
        txtClassName = findViewById(R.id.className);
        txtTitle = findViewById(R.id.title);
        txtSubmittedOn = findViewById(R.id.submittedOn);
        txtAssignedOn = findViewById(R.id.assignedOn);
        constraintLayout = findViewById(R.id.detailLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, activityType, subjectName);

        activityItemModels = new ArrayList<>();
        adapter = new CwHwDetailAdapter(context, activityItemModels, Constants.DiaryType.Classwork, this);
        activityItems.setAdapter(adapter);
        activityItems.setLayoutManager(new LinearLayoutManager(context));

        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            //return false;
        } else {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.Directory.Base);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Log.e("TAG", "setHwSubmissionLinks: " + directory);

        }
        GetActivityDetail();

        SetGradient();
        if (activityType.toLowerCase().equals("classwork")) {
            txtSubmittedOn.setVisibility(View.GONE);

            Guideline guideLine = findViewById(R.id.guideline2);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
            params.guidePercent = 1.0f; // 45% // range: 0 <-> 1
            guideLine.setLayoutParams(params);

            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) txtClassName.getLayoutParams();
            params2.rightMargin = 10;
            txtClassName.setLayoutParams(params2);
        } else setHwSubmissionLinks();
    }

    /**
     * Set visibility of links
     * Set data to links
     */
    private void setHwSubmissionLinks() {

        if (userData.getRoleTitle().equals(Constants.Role.PARENT)) {
            if (getIntent().hasExtra(Constants.TEACHER_MOBILE)) {
                String teacherMobile = getIntent().getStringExtra(Constants.TEACHER_MOBILE);

                if (!teacherMobile.equals("")) {
                    mViewBinding.whtsapLink.setOnClickListener(this);
                    mViewBinding.whtsapLink.setVisibility(View.VISIBLE);
                    mViewBinding.whtsapIc.setVisibility(View.VISIBLE);

                    String whtsapLink = getString(R.string.whtsapLink, teacherMobile);
                    String whtsapHyperLink = "<a href=\"" + whtsapLink + "\">" + getString(R.string.submitHwByWhtsap) + "</a>";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        mViewBinding.whtsapLink.setText(Html.fromHtml(whtsapHyperLink, Html.FROM_HTML_MODE_LEGACY));
                    else mViewBinding.whtsapLink.setText(Html.fromHtml(whtsapHyperLink));
                }
            }

            if (getIntent().hasExtra(Constants.TEACHER_EMAIL)) {
                teacherEmail = getIntent().getStringExtra(Constants.TEACHER_EMAIL);
                if (!teacherEmail.equals("")) {
                    mViewBinding.emailLink.setPaintFlags(mViewBinding.emailLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    mViewBinding.emailLink.setText(getString(R.string.submitHwByEmail));

                    mViewBinding.emailLink.setOnClickListener(this);
                    mViewBinding.emailLink.setVisibility(View.VISIBLE);
                    mViewBinding.emailIc.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void sendMail(String teacherEmail, String emailSubject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", teacherEmail, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.hwSubmissionSubject, className,
                subjectName, userPref.getSelectedChild().Name, userPref.getSelectedChild().RoleNumber.toString()));
        context.startActivity(Intent.createChooser(emailIntent, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userData.getRoleTitle().equals(Constants.Role.ADMIN) || userData.getRoleTitle().equals(Constants.Role.TEACHER)) {
            getMenuInflater().inflate(R.menu.cw_hw_detail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.icEdit:
                Intent editCwHwIntent = new Intent(context, CreateCwHwActivity.class);
                editCwHwIntent.putExtra("activityId", activityId);
                editCwHwIntent.putExtra("activityType", activityType);
                editCwHwIntent.putExtra("activityTypeId", activityTypeId);
                startActivity(editCwHwIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetActivityDetail() {

        progressBar.setVisibility(View.VISIBLE);

        classworkService.getClassworkDetail(activityId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<ActivityDetailBean>() {
            @Override
            public void onResponse(Call<ActivityDetailBean> call, Response<ActivityDetailBean> response) {
                progressBar.setVisibility(View.GONE);
                activityItemModels.clear();

                ActivityDetailBean classworkDetailBean = response.body();
                if (classworkDetailBean != null) {
                    if (classworkDetailBean.rcode.equals(Constants.Rcode.OK)) {
                        ActivityDetailModel classworkDetailModel = classworkDetailBean.data;
                        activityDetail = classworkDetailModel.activityModel;

                        txtClassName.setText(className);
                        txtTitle.setText(activityDetail.Title);
                        String submissionDate = activityDetail.ActivitySubmissionDate == null ? "" : activityDetail.ActivitySubmissionDate;
                        txtSubmittedOn.setText("Submission\n" + submissionDate);
                        txtAssignedOn.setText("Assigned On\n" + activityDetail.ActivityDate);

                        for (ActivityItemModel item : classworkDetailModel.activityItemModel) {
                            activityItemModels.add(item);
                        }
                        adapter.notifyDataSetChanged();

                    } else if (classworkDetailBean.rcode == Constants.Rcode.NORECORDS) {
                        activityItems.setVisibility(View.GONE);
                        noRecord.setVisibility(View.VISIBLE);
                        noRecordMsg.setText(getString(R.string.noRecordMsg));

                    }
                } else {
                    Toast.makeText(context, "Could not load activity detail. Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ActivityDetailBean> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Could not load activity detail. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PermissionRequestCode.RREAD_WRITE_STORAGE) {
            String msg = "Without these permissions you won't be able to download the attachments. Click OK to grant permission else Cancel.";
            Utility.onRequestPermissionresult(grantResults, context, Constants.ContextTypeForPermissionResult.CW_HW, msg);
        }
    }

    public void SetGradient() {
        int[] colors = {getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)};

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtAssignedOn.setBackground(gd);
            txtSubmittedOn.setBackground(gd);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whtsapLink:
                mViewBinding.whtsapLink.setMovementMethod(LinkMovementMethod.getInstance());
                break;

            case R.id.emailLink:
                sendMail(teacherEmail, getString(R.string.emailSubject));
                break;
        }
    }

    @Override
    public void onItemClick(String title) {
        //
    }
}