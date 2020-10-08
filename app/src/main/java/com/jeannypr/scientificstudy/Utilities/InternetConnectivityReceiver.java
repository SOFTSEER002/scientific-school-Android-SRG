package com.jeannypr.scientificstudy.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/*public class InternetConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (networkChangedListener != null) {
            networkChangedListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApplication.getInstance()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}*/
public class InternetConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = InternetConnectivityReceiver.class.getSimpleName();
    ShowInternetStatus showInternetStatus;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (isNetworkAvailable(context)) {
            Log.i(TAG, "Network Availble");

        } else {
            //TODO: show status in snakebar in activity by implementing interface, jst like:https://stackoverflow.com/questions/31428437/how-to-add-snackbars-in-a-broadcastreceiver
           // showInternetStatus.OnConnectivityChange(false);
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No internet connection");
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public interface ShowInternetStatus {
        public void OnConnectivityChange(boolean status);
    }
}

//import android.content.BroadcastReceiver;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.net.ConnectivityManager;
//        import android.widget.Toast;
//
//public class CheckConnectivity extends BroadcastReceiver{
//
//    @Override
//    public void onReceive(Context context, Intent arg1) {
//
//        boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//        if(isConnected){
//            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
//        }
//        else{
//            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
//        }
//    }
//}