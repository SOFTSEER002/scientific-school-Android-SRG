package com.jeannypr.scientificstudy.BackgroundTask;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Preference.UserPreference;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineStatusWorker  extends Worker {

    ChatService mService;
    UserPreference preference;
    public OnlineStatusWorker(@NonNull Context appContext,@NonNull WorkerParameters params){
        super(appContext,params);
        mService = new DataRepo<>(ChatService.class, appContext, ApiConstants.CHAT_BASE_URL).getService();
        preference = UserPreference.getInstance(appContext);
    }

    private static final String TAG = OnlineStatusWorker.class.getSimpleName();
    @NonNull
    @Override
    public Result doWork() {
        try{

            int userId = preference.getUserId();
            if(preference.isLoggedIn() && userId != 0){
                mService.LogUser(userId,preference.getSchoolCode()).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.i(TAG, "user logged!");
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i(TAG, "user logging failed!");
                    }
                });
            }

            return Worker.Result.success();
        }catch (Throwable throwable){
            Log.e(TAG, "Error updating user log", throwable);
            return Worker.Result.failure();
        }
    }
}