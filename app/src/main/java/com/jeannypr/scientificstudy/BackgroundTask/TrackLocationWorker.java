package com.jeannypr.scientificstudy.BackgroundTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class TrackLocationWorker extends Worker {
    TransportService transportService;
    UserPreference userPref;
    UserModel userModel;
    SchoolDetailModel mSchoolDetail;
    Context context;

    private static final String TAG = TrackLocationWorker.class.getSimpleName();

    public TrackLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        transportService = new DataRepo<>(TransportService.class, context).getService();
        userPref = UserPreference.getInstance(context);
        userModel = userPref.getUserData();
        mSchoolDetail = userPref.getSchoolData();
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "reached in doWork...");

        try {
            final Double[][] latLong = new Double[1][1];
            Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    latLong[0] = Utility.GetLocation(context);
                    //call api
                   /* LocationDetailModel input = new LocationDetailModel();
                    input.setSchoolId(userModel.getSchoolId());
                    input.setJourneyId(userPref.getJourneyDetail().getJourneyId());
                    input.setLatitude(latLong[0][0].toString());
                    input.setLongitude(latLong[0][1].toString());

                    transportService.SaveLocationDetails(input).enqueue(new Callback<Bean>() {
                        @Override
                        public void onResponse(Call<Bean> call, Response<Bean> response) {
                            Bean resp = response.body();

                            if (resp != null) {

                                if (resp.rcode == Constants.Rcode.OK) {
                                    Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();

                                } else {
                                    //Toast.makeText(context, "Failed to saved", Toast.LENGTH_SHORT).show();
                                    Log.e("Track location err : ", "");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Bean> call, Throwable t) {
                            //Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                            Log.e("Track location exc : ", t.getMessage());
                        }
                    });*/
                }
            }, 1000);

            /* latLong = Utility.GetLocation(context);*/

           /* LogDeviceAccessModel logModel = new LogDeviceAccessModel();
            logModel.DeviceToken = userPref.getDeviceToken();
            logModel.Platform = Constants.Platform.ANDROID;
            logModel.UserId = userModel.getUserId();
            logModel.RoleTitle = userModel.getRoleTitle();
            logModel.NetworkType = Utility.isWifiNetworkAvailable(getApplicationContext());
            logModel.SchoolCode = mSchoolDetail.getSchoolKey();
          *//*  logModel.Lat = latLong[0] != null ? latLong[0] : 0;
            logModel.Long = latLong[1] != null ? latLong[1] : 0;*//*
            logModel.Lat = "";
            logModel.Long = "";*/

            /*chatService.SaveLogDeviceAccess(logModel).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i(TAG, "Device access log successfully saved");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i(TAG, "Failed to save device access log");
                }
            });*/

            return Result.success();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception while saving current location", ex);
            return Result.failure();
        }
    }


}
