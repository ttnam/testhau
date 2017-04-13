package io.yostajsc.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nphau on 9/27/2015.
 */
public class StorageUtils {

    protected SharedPreferences preferences = null;

    private static StorageUtils mInstance = null;

    protected StorageUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static StorageUtils inject(Context context) {
        if (mInstance == null) {
            mInstance = new StorageUtils(context);
        }
        return mInstance;
    }

    public int getInt(String key) {
        int value = -1;
        if (preferences.contains(key))
            value = preferences.getInt(key, -1);
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

}