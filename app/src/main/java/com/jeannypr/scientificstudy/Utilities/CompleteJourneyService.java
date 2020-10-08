package com.jeannypr.scientificstudy.Utilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.Transport.api.TransportService;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;
import com.jeannypr.scientificstudy.Transport.model.SaveJourneyBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteJourneyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UpdateJourneyDetail();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void UpdateJourneyDetail() {
        final UserPreference  userPref = UserPreference.getInstance(getApplicationContext());
        TransportService transportService = new DataRepo<>(TransportService.class, getApplicationContext()).getService();
        JourneyDetailModel input = new JourneyDetailModel();

        input.setMode(Constants.JourneyMode.COMPLETE);
        input.setJourneyId(userPref.getJourneyDetail().getJourneyId());

        transportService.SaveJourneyDetails(input).enqueue(new Callback<SaveJourneyBean>() {
            @Override
            public void onResponse(Call<SaveJourneyBean> call, Response<SaveJourneyBean> response) {

                SaveJourneyBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {

                            userPref.setJourneyDetail(new JourneyDetailModel());
                            Intent dateIntent = new Intent(Constants.DATE_CHANGE_INTENT);
                            dateIntent.putExtra("isJourneySessionExpired",true);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(dateIntent);

                        } else {
                           // Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                       // Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                   // Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveJourneyBean> call, Throwable t) {

            }
        });
    }
}