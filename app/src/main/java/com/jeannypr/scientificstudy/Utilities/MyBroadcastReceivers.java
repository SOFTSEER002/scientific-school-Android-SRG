package com.jeannypr.scientificstudy.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.RequestCodes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MyBroadcastReceivers extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast receiver :", "calling onReceive method");

        if (intent.getExtras() != null) {
            switch (intent.getExtras().getInt("requestCode", 0)) {

                case RequestCodes.CLOSE_JOURNEY_ON_EXPIRATION_REQUEST_CODE:
                    Intent dateIntent = new Intent(Constants.DATE_CHANGE_INTENT);

                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    String newDate = df.format(cal.getTime());

                    dateIntent.putExtra("currentDate", newDate);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(dateIntent);
                    break;
            }
        }

    }
}
