package io.yostajsc.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import io.yostajsc.sdk.model.token.IgToken;

/**
 * Created by nphau on 9/27/2015.
 */
public class PrefsUtils {

    private SharedPreferences preferences = null;

    private static PrefsUtils mInstance = null;

    private final static String KEY_TOKEN = "KEY_TOKEN";

    private PrefsUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefsUtils inject(Context context) {
        if (mInstance == null) {
            mInstance = new PrefsUtils(context);
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

    public boolean getBoolean(String key) {
        boolean value = false;
        if (preferences != null && preferences.contains(key))
            value = preferences.getBoolean(key, false);
        return value;
    }

    public boolean save(String key, int value) {
        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        return true;
    }

    public boolean save(String key, boolean value) {
        if (preferences == null) return false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
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


    public void setToken(IgToken igToken) {
        if (preferences == null) return;

        Gson gson = new Gson();
        String json = gson.toJson(igToken);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN, json);
        editor.apply();
    }

    public IgToken getToken() {
        Gson gson = new Gson();
        IgToken igToken = null;
        if (preferences != null && preferences.contains(KEY_TOKEN)) {
            String json = preferences.getString(KEY_TOKEN, "");
            igToken = gson.fromJson(json, IgToken.class);
        }
        return igToken;
    }

    public void removeAll() {
        removes(KEY_TOKEN);
    }
}