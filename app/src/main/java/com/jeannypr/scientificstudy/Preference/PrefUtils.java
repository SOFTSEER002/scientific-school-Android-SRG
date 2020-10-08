package com.app.help.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by JeannyPrAndroid on 26-Sep-17.
 */

public class PrefUtils {


    public static final String GCM_ID="GCM_ID";

    public static final String MenuResponse="menudata";
    public static final String MenuCategory="menuCategory";

    public static final String singleProduct="singleProduct";

    public static final String USER_INFO="userInfo";
    public static final String operatorLocation="operatorlocation";
    public static final String planValue="planAmount";
    public static final String InstallEvent="installEvent";

    public static final String GOOGLE_ADVER_ID="GOOGLE_ADVER_ID";
    public static final String PUBLIC_IP="P_IP";
    public static final String OTP="otp";
    public static final String DEVICE_TOKEN="device_token";
    public static final String USER_ID="user_id";
    public static final String ID="id"; // UserId
    public static final String WISH_COUNT="wish_count";
    public static final String CART_COUNT="cart_count";
    public static final String M_NUmBER="m_number";
    public static final String USER_IMAGE="userimg";
    public static final String BALANCE_WALLET="wallet_bal";

    public static final String BANK_INFO="bank_info";
    public static final String PLAN_DATA="plan_data";



    //
    public static final String PREFS_LOGIN_Latitude_KEY = "__Latitude__";
    public static final String PREFS_LOGIN_Longitude_KEY = "__Longitude__";
    public static final String PREFS_LOGIN_PICProfile_KEY = "__PICProfile__";


    public static void saveToPrefs(Context context, String key, String value) {
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            if(null!=prefs){
                final SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value);
                editor.commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getFromPrefs(Context context, String key,
                                      String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {

            return sharedPrefs.getString(key, defaultValue);

        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
