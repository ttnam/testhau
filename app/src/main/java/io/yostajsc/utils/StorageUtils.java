package io.yostajsc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.models.User;

/**
 * Created by nphau on 9/27/2015.
 */
public class StorageUtils {

    public static final String KEY_USER = "KEY_USER";

    public static int KEY_LANGUAGE_MODE = 1;

    private SharedPreferences preferences = null;

    private static StorageUtils mInstance = null;

    private StorageUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static StorageUtils inject(Context context) {
        if (mInstance == null) {
            mInstance = new StorageUtils(context);
        }
        return mInstance;
    }

    public int getInt(String key) {
        int value = 0;
        if (preferences.contains(key))
            value = preferences.getInt(key, 0);
        return value;
    }

    public String getString(String key) {
        String value = null;
        if (preferences != null && preferences.contains(key))
            value = preferences.getString(key, "");
        return value;
    }

    public boolean save(String key, int value) {
        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        return true;
    }

    public boolean save(String key, String value) {

        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean removes(String... keys) {
        if (preferences == null) return false;
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
        return true;
    }

    public boolean save(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return save(AppDefine.USER, json);
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = getString(AppDefine.USER);
        return gson.fromJson(json, User.class);
    }
}
/*

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
*/

/*

    public String onGetAppSetting() {
        return getSettingString(KEY_APP_SETTING);
    }
*/
/*
    public boolean onApplyAppSetting(String json) {
        return saveSetting(KEY_APP_SETTING, json);
    }*/