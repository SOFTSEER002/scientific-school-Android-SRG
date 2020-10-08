package com.jeannypr.scientificstudy.BackgroundTask;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryBean;
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSessionWorker extends Worker {
    UserPreference userPref;
    UserModel userData;

    private static final String TAG = CheckSessionWorker.class.getSimpleName();

    public CheckSessionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        userPref = UserPreference.getInstance(context);
        userData = userPref.getUserData();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            CheckSession();
            return Result.success();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception while checking session status", ex);
            return Result.failure();
        }
    }

    private void CheckSession() {
        if (userPref.isLoggedIn()) {
            userData = userPref.getUserData();

            if (userData.getUserId() != -1) {
                if (!userPref.IsSessionExpired()) {
                    GetSessionCurrentState();
                }
            }
        }
    }

    private void GetSessionCurrentState() {
        LoginService service = new DataRepo<>(LoginService.class, getApplicationContext()).getService();
        service.CheckSessionExpiry(userData.getUserId()).enqueue(new Callback<CheckSessionExpiryBean>() {
            @Override
            public void onResponse(Call<CheckSessionExpiryBean> call, Response<CheckSessionExpiryBean> response) {

                if (response.body() != null) {
                    CheckSessionExpiryBean bean = response.body();

                    if (bean != null && bean.rcode == Constants.Rcode.OK) {
                        if (bean.data != null) {
                            CheckSessionExpiryModel expiryModel = bean.data;

                            if (expiryModel.isLoginExpired()) {
                                userPref.SetIsSessionExpired(true);
                            } else {

                                if (expiryModel.isCurrentAcademicYearId() != userData.getAcademicyearId()) {
                                    userPref.SetIsSessionExpired(true);
                                } else {
                                    userPref.SetIsSessionExpired(false);
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<CheckSessionExpiryBean> call, Throwable t) {
                Log.e("Check session validity:", t.getMessage());
            }
        });
    }
}
