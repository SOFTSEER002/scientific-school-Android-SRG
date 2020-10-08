package com.jeannypr.scientificstudy.BackgroundTask;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Login.model.LogDeviceModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceLogWorker extends Worker {
    ChatService chatService;
    UserPreference userPref;
    private static final String TAG = DeviceLogWorker.class.getSimpleName();

    public DeviceLogWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        chatService = new DataRepo<>(ChatService.class, context, ApiConstants.CHAT_BASE_URL).getService();
        userPref = UserPreference.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            /*String arch = System.getProperty("os.arch");
            String java = System.getProperty("java.runtime.version");
            String osName = System.getProperty("os.name");
            String sdk = String.valueOf(android.os.Build.VERSION.SDK_INT);
            info.OS = Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
            info.Manufacturer = android.os.Build.MANUFACTURER;
            info.Model = android.os.Build.MODEL;
            info.Serial = android.os.Build.SERIAL;
            info.KernalVersion = System.getProperty("os.version");
            info.ConnectivityType = isWifiNetworkAvailable(context);
            info.BluetoothStatus = Utility.CheckBluetoothConnection();*/

            LogDeviceModel logModel = new LogDeviceModel();
            logModel.DeviceToken = userPref.getDeviceToken();
            logModel.Platform = Constants.Platform.ANDROID;
            logModel.DeviceModel = android.os.Build.MODEL;
            logModel.SDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
            logModel.OS = Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

            chatService.SaveLogDevice(logModel).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i(TAG, "Device log successfully saved");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i(TAG, "Failed to save device log");
                }
            });

            return Worker.Result.success();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception while saving device log", ex);
            return Worker.Result.failure();
        }
    }
}
