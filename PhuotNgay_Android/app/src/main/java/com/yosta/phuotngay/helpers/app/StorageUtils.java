package com.yosta.phuotngay.helpers.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.yosta.phuotngay.firebase.model.FirebaseUser;

import java.util.Locale;

/**
 * Created by nphau on 9/27/2015.
 */
public class StorageUtils {

    public static final String KEY_USER = "KEY_USER";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_LANGUAGE = "LANGUAGE";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    public static final String KEY_USER_MEMBER_SHIP = "KEY_USER_MEMBER_SHIP";
    public static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";
    public static final String KEY_USER_GENDER = "KEY_USER_GENDER";
    public static final String KEY_APP_SETTING = "KEY_APP_SETTING";
    public static final String KEY_USER_COVER_URL = "KEY_USER_COVER_URL";
    public static final String KEY_USER_AVATAR_URL = "KEY_USER_AVATAR_URL";


    public static int KEY_LANGUAGE_MODE = 1;

    private Context mContext;
    private SharedPreferences preferences = null;

    public StorageUtils(Application application) {
        preferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public StorageUtils(Context context) {
        this.mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public int getSettingInt(String key) {
        int value = 0;
        if (preferences.contains(key))
            value = preferences.getInt(key, 0);
        return value;
    }

    public static void saveSetting(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void changeAppLanguage() {
        try {
            saveSetting(KEY_LANGUAGE, KEY_LANGUAGE_MODE);
            String keyText = (StorageUtils.KEY_LANGUAGE_MODE == 0) ? "vi" : "en";
            Resources res = this.mContext.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale(keyText.toLowerCase());
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSettingString(String key) {

        String value = null;

        if (preferences != null && preferences.contains(key))
            value = preferences.getString(key, "");

        return value;
    }

    public String onGetAppSetting() {
        return getSettingString(KEY_APP_SETTING);
    }

    public boolean saveSetting(String key, int value) {
        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        return true;
    }

    public boolean saveSetting(String key, String value) {

        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean removeSettings(String... keys) {
        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();

        return true;
    }

    public boolean onApplyAppSetting(String json) {
        return saveSetting(KEY_APP_SETTING, json);
    }

    public FirebaseUser getUser() {
        Gson gson = new Gson();
        String json = getSettingString(KEY_USER);
        return gson.fromJson(json, FirebaseUser.class);
    }

    public void setUser(FirebaseUser user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        saveSetting(KEY_USER, json);
    }
}
