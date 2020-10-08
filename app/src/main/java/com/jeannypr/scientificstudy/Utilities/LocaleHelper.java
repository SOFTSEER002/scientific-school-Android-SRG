package com.jeannypr.scientificstudy.Utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import com.jeannypr.scientificstudy.Preference.UserPreference;

import java.util.Locale;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * <p/>
 * You can also change the locale of your application on the fly by using the setLocale method.
 * <p/>
 * Created by gunhansancar on 07/10/15.
 */
public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Language.Helper.Selected.Language";

    // returns Context having application default locale for all activities
    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLanguage(context, lang);
    }

    // sets application locale with default locale persisted in preference manager on each new launch of application and
    // returns Context having application default locale
    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLanguage(context, lang);
    }

    // returns language code persisted in preference manager
    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    // persists new language code change in preference manager and updates application default locale
    // returns Context having application default locale
    public static Context setLanguage(Context context, String languageISOCode) {
        persist(context, languageISOCode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, languageISOCode);
        }

        return updateResourcesLegacy(context, languageISOCode);
    }

    // returns language code persisted in preference manager
    public static String getPersistedData(Context context, String defaultLanguage) {
        //   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
        UserPreference preferences = UserPreference.getInstance(context);
        return preferences.GetLanguaugeISOCode();
    }

    // persists new language code in preference manager
    private static void persist(Context context, String languageISOCode) {
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();*/

        UserPreference preferences = UserPreference.getInstance(context);
        preferences.SetLanguage(languageISOCode);
    }

    // For android device versions above Nougat (7.0)
    // updates application default locale configurations and
    // returns new Context object for the current Context but whose resources are adjusted to match the given Configuration
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {

        Locale locale = new Locale(language);

        Configuration configuration = context.getResources().getConfiguration();

        LocaleList localeList = new LocaleList(locale);
        localeList.setDefault(localeList);
        configuration.setLocales(localeList);

        return context.createConfigurationContext(configuration);
    }

    // For android device versions below Nougat (7.0)
    // updates application default locale configurations and
    // returns new Context object for the current Context but whose resources are adjusted to match the given Configuration
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

   /* private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    static UserPreference preferences;
    static LocaleHelper instance;
    Context context;

    public LocaleHelper(Context context) {
        preferences = UserPreference.getInstance(context);
        this.context = context;
    }

    public static LocaleHelper GetInstance(Context context) {
        if (instance == null) {
            instance = new LocaleHelper(context);
        }
        return instance;
    }

    // returns Context having application default locale for all activities
    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    // sets application locale with default locale persisted in preference manager on each new launch of application and
    // returns Context having application default locale
    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    *//*   public Context setLocale(String language, Context baseContext) {*//*
    public static Context setLocale(Context context, String language) {
        persist(context, language);
        *//*  Context context;*//*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return updateResourcesLegacy(context, language);
        }
        //return context;
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        preferences = UserPreference.getInstance(context);
        return preferences.GetLanguauge();
    }

    public static void persist(Context context, String language) {
        preferences = UserPreference.getInstance(context);
        preferences.SetLanguage(language);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
       *//* Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);*//*

        Locale locale = new Locale(language);

        Configuration configuration = context.getResources().getConfiguration();

        LocaleList localeList = new LocaleList(locale);
        localeList.setDefault(localeList);
        configuration.setLocales(localeList);

        return context.createConfigurationContext(configuration);

       *//* Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        return context;*//*
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }*/
}
