package com.jeannypr.scientificstudy.Utilities;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryBean;
import com.jeannypr.scientificstudy.Login.model.CheckSessionExpiryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSessionService extends IntentService {
    UserPreference userPref;
    UserModel model;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CheckSessionService(String name) {
        super(name);
    }

    public CheckSessionService() {
        super("default");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        userPref = UserPreference.getInstance(getApplicationContext());
        CheckSession();
    }

    private void CheckSession() {
        if (userPref.isLoggedIn()) {
            model = userPref.getUserData();

            if (model.getUserId() != -1) {
                if (!userPref.IsSessionExpired()) {
                    GetSessionCurrentState();
                }
            }
        }
    }

    /**
     * Call api to check session expiration state
     * if Academic year or password or user name changed then expire session.
     */
    private void GetSessionCurrentState() {
        LoginService service = new DataRepo<>(LoginService.class, getApplicationContext()).getService();
        service.CheckSessionExpiry(model.getUserId()).enqueue(new Callback<CheckSessionExpiryBean>() {
            @Override
            public void onResponse(Call<CheckSessionExpiryBean> call, Response<CheckSessionExpiryBean> response) {

                if (response.body() != null) {
                    CheckSessionExpiryBean bean = response.body();

                    if (bean.rcode == Constants.Rcode.OK) {
                        if (bean.data != null) {
                            CheckSessionExpiryModel expiryModel = bean.data;

                            if (expiryModel.isLoginExpired()) {
                                userPref.SetIsSessionExpired(true);
                            } else if (expiryModel.isCurrentAcademicYearId() != model.getAcademicyearId()) {
                                userPref.SetIsSessionExpired(true);
                            } else {

                                if (model.getUserName().isEmpty())
                                    model.setUserName(expiryModel.getUserName());
                                else if (!expiryModel.getUserName().equals(model.getUserName()))
                                    userPref.SetIsSessionExpired(true);
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