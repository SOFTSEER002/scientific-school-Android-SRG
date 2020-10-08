package com.jeannypr.scientificstudy.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.ChildModel;
import com.jeannypr.scientificstudy.Base.Model.UserGuidanceDetail;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Dashboard.model.DashboardModulesModel;
import com.jeannypr.scientificstudy.Dashboard.model.FamilyMembersModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class UserPreference {

    private SharedPreferences preferences;
    static UserPreference instance;

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_SCHOOL_KEY_AVAILABLE = "IsSchoolKeyAvailable";
    private static final String SCHOOL_DATA = "SchoolData";
    private static final String SCHOOL_CODE = "SchoolCode";
    private static final String USER_DATA = "UserData";
    private static final String FAMILY_MEMBERS_DATA = "familyMembersData";
    private static final String CHILDREN = "Children";
    private static final String SELECTED_CHILD = "SelectedChild";
    private static final String IS_SCHOOL_CODE_REQUIRED = "isSchoolKeyRequired";
    private static final String IS_APP_INSTALLED = "IsAppInstalled";
    private static final String CURRENT_CHAT_ROOM_ID = "CurrentChatRoomId";
    private static final String DEVICE_TOKEN_PREV = "previousDeviceToken";
    private static final String DEVICE_TOKEN_CURRENT = "newDeviceToken";
    private static final String GUIDE_USER = "guideUser";

    private static final String IS_JOURNEY_STARTED = "IsJourneyStarted";
    private static final String JOURNEY_DETAIL = "JourneyDetail";
    private static final String LANGUAGE_ISO_CODE = "languageIsoCode";
    private static final String LANGUAGE_NAME = "languageName";
    private static final String IS_SESSION_EXPIRED = "isSessionExpired";
    private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";
    private static final String ALARM_OPERATION = "alarmOperation";
    private static final String TOTAL_UNREAD_MSG = "totalUnreadMessages";
    private static final String PERMISSION_DATA = "module_permission_data";


    public synchronized void SetTotalUnreadMessages(int total) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TOTAL_UNREAD_MSG, total);
        editor.apply();
    }

    public synchronized int GetUnreadMessages() {
        return preferences.getInt(TOTAL_UNREAD_MSG, 0);
    }

    public synchronized void SetSubscriptionTopic(String topic) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SUBSCRIPTION_TOPIC, topic);
        editor.apply();
    }

    public synchronized String GetSubscriptionTopic() {
        return preferences.getString(SUBSCRIPTION_TOPIC, "");
    }

    public synchronized void SetIsSessionExpired(Boolean currentState) {
        SharedPreferences.Editor editor = preferences.edit();
        Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);

        if (isLogin) {
            editor.putBoolean(IS_SESSION_EXPIRED, currentState);
        } else {
            editor.putBoolean(IS_SESSION_EXPIRED, false);
        }

       /* if (currentState) {
            editor.putInt(ALARM_OPERATION, Constants.Alarm.CANCEL);
        }*/
        editor.apply();
    }

    public synchronized Boolean IsSessionExpired() {
        return preferences.getBoolean(IS_SESSION_EXPIRED, false);
    }

    public synchronized void SetLanguage(String langIsoCode) {
        SharedPreferences.Editor editor = preferences.edit();
        // editor.putString(LANGUAGE_NAME, lang);
        editor.putString(LANGUAGE_ISO_CODE, langIsoCode);
        editor.commit();
    }

    public synchronized String GetLanguauge() {
        return preferences.getString(LANGUAGE_NAME, Constants.Languages.ENGLISH);
    }

    public synchronized String GetLanguaugeISOCode() {
        return preferences.getString(LANGUAGE_ISO_CODE, Constants.LanguagesISOCode.ENGLISH);
    }

    /* public synchronized boolean IsUserGuided() {
         return preferences.getBoolean(GUIDE_USER, false);
     }

     public synchronized void setUserGuided(boolean b) {
         preferences.edit().putBoolean(GUIDE_USER, b).apply();
     }*/
    public synchronized UserGuidanceDetail GetUserGuidanceDetail() {
        return new Gson().fromJson(preferences.getString(GUIDE_USER, ""), UserGuidanceDetail.class);
    }

    public synchronized void SetUserGuidanceDetail(UserGuidanceDetail detail) {
        if (detail == null) {
            preferences.edit().putString(GUIDE_USER, "").apply();
        } else {
            preferences.edit().putString(GUIDE_USER, new Gson().toJson(detail)).apply();
        }
    }

    private UserPreference(Context context) {
        if (context != null) {
            preferences = context.getSharedPreferences("saleorder_preferences", Context.MODE_PRIVATE);
        }
    }

    public static synchronized UserPreference getInstance(Context context) {
        instance = instance == null ? new UserPreference(context) : instance;
        return instance;
    }

    public synchronized String getSchoolCode() {
        String schoolCode = preferences.getString(SCHOOL_CODE, "");
        schoolCode = schoolCode.replaceAll("\\s", "");
        return schoolCode;
    }

    public synchronized void setSchoolCode(String schoolCode) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SCHOOL_CODE, schoolCode);
        editor.apply();
    }

    public synchronized void SetFieldForApiHeader(Boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SCHOOL_CODE_REQUIRED, b);
        editor.apply();
    }

    public synchronized boolean IsSchoolCodeRequiredInApiCall() {
        return preferences.getBoolean(IS_SCHOOL_CODE_REQUIRED, true);
    }

    public synchronized void setSchoolData(SchoolDetailModel detail) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SCHOOL_KEY_AVAILABLE, true);
        editor.putString(SCHOOL_CODE, detail.SchoolKey);
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        editor.putString(SCHOOL_DATA, json);
        editor.commit();
    }

    public synchronized SchoolDetailModel getSchoolData() {
        Boolean isAvailable = preferences.getBoolean(IS_SCHOOL_KEY_AVAILABLE, false);
        if (isAvailable) {
            String json = preferences.getString(SCHOOL_DATA, "");
            Gson gson = new Gson();
            SchoolDetailModel detail = gson.fromJson(json, SchoolDetailModel.class);
            return detail;
        }
        return new SchoolDetailModel();
    }

    public synchronized void removeSchoolData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SCHOOL_KEY_AVAILABLE, false);
        editor.putString(SCHOOL_DATA, "");
        editor.putString(SCHOOL_CODE, "");
        editor.commit();
    }

    public synchronized void setUserData(UserModel userData) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        editor.putString(USER_DATA, json);
        editor.commit();
    }

    public synchronized UserModel getUserData() {
        Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);
        if (isLogin) {
            String json = preferences.getString(USER_DATA, "");
            Gson gson = new Gson();
            UserModel detail = gson.fromJson(json, UserModel.class);
            return detail;
        }
        return new UserModel();
    }

    public synchronized void setFamilyMembersData(ArrayList<FamilyMembersModel> userData) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userData);
        editor.putString(FAMILY_MEMBERS_DATA, json);
        editor.apply();
    }

    public synchronized ArrayList<FamilyMembersModel> getFamilyMembersData() {
        String json = preferences.getString(FAMILY_MEMBERS_DATA, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<FamilyMembersModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public synchronized void setJourneyDetail(JourneyDetailModel detail) {
        SharedPreferences.Editor editor = preferences.edit();

        if (detail.getJourneyId() > 0) {
            editor.putBoolean(IS_JOURNEY_STARTED, true);
        } else {
            editor.putBoolean(IS_JOURNEY_STARTED, false);
        }

        Gson gson = new Gson();
        String json = gson.toJson(detail);
        editor.putString(JOURNEY_DETAIL, json);
        editor.commit();
    }

    public synchronized JourneyDetailModel getJourneyDetail() {
        Boolean isJourneyStarted = preferences.getBoolean(IS_JOURNEY_STARTED, false);
        if (isJourneyStarted) {
            String json = preferences.getString(JOURNEY_DETAIL, "");
            Gson gson = new Gson();
            JourneyDetailModel model = gson.fromJson(json, JourneyDetailModel.class);
            return model;
        }
        return new JourneyDetailModel();
    }

    public synchronized Boolean IsJourneyStarted() {
        return preferences.getBoolean(IS_JOURNEY_STARTED, false);
    }

    public synchronized int getUserId() {

        Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);
        if (isLogin) {
            String json = preferences.getString(USER_DATA, "");
            Gson gson = new Gson();
            UserModel detail = gson.fromJson(json, UserModel.class);
            return detail.getUserId();
        }
        return new UserModel().getUserId();
    }

    public synchronized String getUserName() {

        try {
            Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);
            if (isLogin) {
                String json = preferences.getString(USER_DATA, "");
                Gson gson = new Gson();
                UserModel detail = gson.fromJson(json, UserModel.class);
                return detail.getFirstName() + " " + detail.getLastName();
            }
        } catch (Exception ex) {
            return "";
        }
        return "";
    }

    //for children list in case of parent login

    public synchronized void setChildren(List<ChildModel> childData) {

        SharedPreferences.Editor editor = preferences.edit();

        if (childData == null) {
            editor.putString(CHILDREN, "");

        } else {
            editor.putBoolean(IS_LOGIN, true);
            Gson gson = new Gson();
            String json = gson.toJson(childData);
            editor.putString(CHILDREN, json);
        }

        editor.commit();
    }

    public synchronized List<ChildModel> getChildren() {
        Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);
        if (isLogin) {
            Gson gson = new Gson();
            String json = preferences.getString(CHILDREN, "");

            Type type = new TypeToken<List<ChildModel>>() {
            }.getType();
            List<ChildModel> detail = gson.fromJson(json, type);
            return detail;
        }
        List<ChildModel> childModel = new ArrayList<>();
        return childModel;
    }

    public synchronized void setSelectedChild(ChildModel childModel) {
        SharedPreferences.Editor editor = preferences.edit();
        if (childModel == null) {
            editor.putString(SELECTED_CHILD, "");
        } else {
            editor.putBoolean(IS_LOGIN, true);
            Gson gson = new Gson();
            String json = gson.toJson(childModel);
            editor.putString(SELECTED_CHILD, json);
        }

        editor.commit();
    }

    public synchronized ChildModel getSelectedChild() {
        Boolean isLogin = preferences.getBoolean(IS_LOGIN, false);
        if (isLogin) {
            String json = preferences.getString(SELECTED_CHILD, "");
            Gson gson = new Gson();
            ChildModel detail = gson.fromJson(json, ChildModel.class);
            return detail;
        }
        return new ChildModel();
    }

    public synchronized Boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public synchronized Boolean isSchoolKeyAvailable() {
        return preferences.getBoolean(IS_SCHOOL_KEY_AVAILABLE, false);
    }

    public synchronized void logOut() {
        setChildren(null);
        setSelectedChild(null);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(USER_DATA, "");
        editor.putBoolean(IS_SESSION_EXPIRED, false);
        editor.apply();
    }

    public synchronized void clearData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

   /* public synchronized Boolean IsPackageInstalled() {
        return preferences.getBoolean(IS_APP_INSTALLED, false);
    }

    public synchronized void SetPackageState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_APP_INSTALLED, true);
        editor.apply();
    }*/

    public synchronized void SetCurrentChatRoomId(int roomId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_CHAT_ROOM_ID, roomId);
        editor.apply();
    }

    public synchronized int GetCurrentChatRoomId() {
        return preferences.getInt(CURRENT_CHAT_ROOM_ID, 0);
    }

    public synchronized String getDeviceToken() {
        return preferences.getString(DEVICE_TOKEN_CURRENT, "");
    }

    public synchronized void setDeviceToken(String oldToken, String newToken) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_TOKEN_CURRENT, newToken);
        editor.putString(DEVICE_TOKEN_PREV, oldToken);
        editor.apply();
    }

    public synchronized void setPermissionData(ArrayList<DashboardModulesModel> detail) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        editor.putString(PERMISSION_DATA, json);
        editor.apply();
    }

    public synchronized ArrayList<DashboardModulesModel> getPermissionData() {
        String json = preferences.getString(PERMISSION_DATA, "");
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<DashboardModulesModel>>() {
        }.getType();
        ArrayList<DashboardModulesModel> detail = gson.fromJson(json, type);
        return detail;
    }
}
