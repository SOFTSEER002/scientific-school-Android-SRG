package com.jeannypr.scientificstudy.Dashboard.Activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.NotificationSettingBean;
import com.jeannypr.scientificstudy.Dashboard.model.NotificationSettingInputModel;
import com.jeannypr.scientificstudy.Dashboard.model.NotificationSettingModel;
import com.jeannypr.scientificstudy.Dashboard.model.SettingBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public Context context;
    UserModel userData;
    UserPreference userPref;
    AppSettingService settingService;
    CheckBox notifyOnCompleteChk, notifyOnStartChk;
    int notifyOnCompleteJourney, notifyOnStartJourney;
    TextView saveBtn;
    ProgressBar pb;
    ArrayList<NotificationSettingModel> settingModels;
    ConstraintLayout notifyOnStartRow, notifyOnCompleteRow;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_notification_setting);

        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
        settingService = new DataRepo<>(AppSettingService.class, context).getService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.SetToolbar(context, "Settings", "");

        notifyOnStartChk = findViewById(R.id.notifyOnStartChk);
        notifyOnCompleteChk = findViewById(R.id.notifyOnCompleteChk);
        notifyOnCompleteChk.setOnCheckedChangeListener(this);
        notifyOnStartChk.setOnCheckedChangeListener(this);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        pb = findViewById(R.id.progressBar);

        GetSettings();
    }


    private void GetSettings() {
        //get settings and bind with UI
        settingModels = new ArrayList<>();
        pb.setVisibility(View.VISIBLE);

        settingService.GetNotificationSettings(userData.getSchoolId()).enqueue(new Callback<SettingBean>() {
            @Override
            public void onResponse(Call<SettingBean> call, Response<SettingBean> response) {
                SettingBean bean = response.body();

                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {

                        if (bean.data != null) {
                            if (bean.data.getNotifications().getNotifyOnStartJourney()) {
                                notifyOnStartChk.setChecked(true);
                                notifyOnStartJourney = 1;
                            } else {
                                notifyOnStartChk.setChecked(false);
                                notifyOnStartJourney = 0;
                            }

                            if (bean.data.getNotifications().getNotifyOnCompleteJourney()) {
                                notifyOnCompleteChk.setChecked(true);
                                notifyOnCompleteJourney = 1;
                            } else {
                                notifyOnCompleteChk.setChecked(false);
                                notifyOnCompleteJourney = 0;
                            }
                        }
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SettingBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SaveNotificationSetting() {
        NotificationSettingInputModel input = new NotificationSettingInputModel();

        input.setModule(Constants.ModuleSetting.TRANSPORT);
        input.setSchoolid(userData.getSchoolId());
        input.setNotifyOnCompleteJourney(notifyOnCompleteJourney);
        input.setNotifyOnStartJourney(notifyOnStartJourney);

        saveBtn.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        settingService.SaveAppSetting(input).enqueue(new Callback<NotificationSettingBean>() {
            @Override
            public void onResponse(Call<NotificationSettingBean> call, Response<NotificationSettingBean> response) {
                NotificationSettingBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, R.string.appsetting_successMsg, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, R.string.appsetting_failedMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                }
                saveBtn.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<NotificationSettingBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        saveBtn.setVisibility(View.VISIBLE);

        switch (compoundButton.getId()) {
            case R.id.notifyOnStartChk:
                //  startjourneyCheck = isChecked;

                if (isChecked) {
                    notifyOnStartJourney = 1;
                } else {
                    notifyOnStartJourney = 0;
                }
                break;

            case R.id.notifyOnCompleteChk:
                //  completejourneyCheck = isChecked;

                if (isChecked) {
                    notifyOnCompleteJourney = 1;

                } else {
                    notifyOnCompleteJourney = 0;
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:
                SaveNotificationSetting();
                break;
        }
    }
}