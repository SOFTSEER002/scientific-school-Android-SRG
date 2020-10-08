package com.jeannypr.scientificstudy.BackgroundTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CheckNetworkConnectivityWorker extends Worker {
    private static final String TAG = CheckNetworkConnectivityWorker.class.getSimpleName();

    public CheckNetworkConnectivityWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (!isNetworkAvailable()) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("No internet connection");
                builder.setMessage("Something went wrong. Your internet connection preventing data from loading. Please check your internet connection.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
              //  builder.show();

                Log.i(TAG, "************************************* No internet connection. ********************************************");

            } else {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("No internet connection");
                        builder.setMessage("Something went wrong. Your internet connection preventing data from loading. Please check your internet connection.");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                      //  builder.show();
                    }
                });

                Log.i(TAG, "********************************** Internet connection. *************************************************");
            }

            return Result.success();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "******************************** Exception : " + ex.getMessage() + "************************************");

            return Result.failure();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isInternet = networkInfo != null && networkInfo.isConnected();
        return isInternet;
    }
}
