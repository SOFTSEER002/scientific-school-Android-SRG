package com.jeannypr.scientificstudy.BackgroundTask;

import android.content.Context;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Login.model.LogDeviceAccessModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.Utilities.Utility;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAccessLogWorker extends Worker {
    ChatService chatService;
    UserPreference userPref;
    UserModel userModel;
    SchoolDetailModel mSchoolDetail;

    private static final String TAG = DeviceAccessLogWorker.class.getSimpleName();

    public DeviceAccessLogWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        chatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();
        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        mSchoolDetail = userPref.getSchoolData();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {

            Double latLong[];
            //    latLong = Utility.GetLocation(getApplicationContext());

            LogDeviceAccessModel logModel = new LogDeviceAccessModel();
            logModel.DeviceToken = userPref.getDeviceToken();
            logModel.Platform = Constants.Platform.ANDROID;
            logModel.UserId = userModel.getUserId();
            logModel.RoleTitle = userModel.getRoleTitle();
            logModel.NetworkType = Utility.isWifiNetworkAvailable(getApplicationContext());
            logModel.SchoolCode = mSchoolDetail.getSchoolKey();
          /*  logModel.Lat = latLong[0] != null ? latLong[0] : 0;
            logModel.Long = latLong[1] != null ? latLong[1] : 0;*/
            logModel.Lat = "";
            logModel.Long = "";

            chatService.SaveLogDeviceAccess(logModel).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i(TAG, "Device access log successfully saved");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i(TAG, "Failed to save device access log");
                }
            });

            return Result.success();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception while saving device access log", ex);
            return Result.failure();
        }
    }


}
