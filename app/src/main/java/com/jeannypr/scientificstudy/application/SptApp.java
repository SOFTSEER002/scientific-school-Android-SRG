
package com.jeannypr.scientificstudy.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeannypr.scientificstudy.BackgroundTask.OnlineStatusWorker;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.BuildConfig;
import com.jeannypr.scientificstudy.Utilities.ForceUpdateChecker;
import com.jeannypr.scientificstudy.Utilities.LocaleHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.multidex.MultiDexApplication;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static com.jeannypr.scientificstudy.Utilities.TrackLocationTask.TAG;

public class SptApp extends MultiDexApplication {
    Context context;
    private WorkManager mWorkManager;

    public void onCreate() {
        super.onCreate();
        context = this;
        mWorkManager = WorkManager.getInstance();

        ScheduleOnlineStatusWorker();
        SetDefaultParamsOnFCM();
       // SetLocale();
        /* CheckSessionExpiry();*/
    }

    private void SetDefaultParamsOnFCM() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION_CODE, BuildConfig.VERSION_CODE);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, Constants.APP_PLAYSTORE_URL);

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        /*firebaseRemoteConfig.setDefaults(R.xml.fcm_params);*/

        firebaseRemoteConfig.fetch(Constants.CACHE_EXPIRATION_FCM_CONFIG) // fetch every second
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Remote config status : fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }

    private void ScheduleOnlineStatusWorker() {

        Constraints networkConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        //the minimum interval is set to 15 mins. I think in the later version of this library
        //it will allow us to have lower intervals.
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(OnlineStatusWorker.class, 15,
                        TimeUnit.MINUTES)
                        .setConstraints(networkConstraints)
                        .build();

        mWorkManager.enqueue(periodicWorkRequest);
    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SetLocale();
    }*/

    /*private Context SetLocale() {
        LocaleHelper instance = LocaleHelper.GetInstance(context);
        String defaultLang = instance.getLanguage();
        if (defaultLang != null) {
            *//*  return instance.setLocale(defaultLang, getBaseContext());*//*
            return instance.setLocale(context,defaultLang);
        }
        return null;
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,LocaleHelper.getLanguage(base)));
        //MultiDex.install(this);
    }
}