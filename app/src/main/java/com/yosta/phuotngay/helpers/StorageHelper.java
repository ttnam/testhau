package com.yosta.phuotngay.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.validate.ValidateHelper;

/**
 * Created by nphau on 9/27/2015.
 */
public class StorageHelper {

    public static final String KEY_USER = "KEY_USER";

    public static int KEY_LANGUAGE_MODE = 1;

    private SharedPreferences preferences = null;

    private static StorageHelper mInstance = null;

    private StorageHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static StorageHelper inject(Context context) {
        if (mInstance == null) {
            mInstance = new StorageHelper(context);
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

    public boolean IsUserLogin() {
        return ValidateHelper.IsNotEmpty(getString(User.AUTHORIZATION));
    }
}
/*

    public void changeAppLanguage() {
        try {
            saveSetting(KEY_LANGUAGE, KEY_LANGUAGE_MODE);
            String keyText = (StorageHelper.KEY_LANGUAGE_MODE == 0) ? "vi" : "en";
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