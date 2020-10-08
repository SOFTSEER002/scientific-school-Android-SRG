package com.jeannypr.scientificstudy.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//"com.jeannypr.scientificstudy.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    public NetworkChangeReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE,  Utility.isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }


}