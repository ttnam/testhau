package io.yostajsc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.models.user.User;

/**
 * Created by nphau on 3/19/17.
 */

public class UserPref {

    private SharedPreferences preferences = null;

    private static UserPref mInstance = null;

    private UserPref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static UserPref inject(Context context) {
        if (mInstance == null) {
            mInstance = new UserPref(context);
        }
        return mInstance;
    }

    public boolean save(User user) {

        if (preferences == null) return false;

        Gson gson = new Gson();
        String json = gson.toJson(user);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConfig.USER, json);
        editor.apply();

        return true;

    }

    public User getUser() {
        Gson gson = new Gson();

        User user = null;
        if (preferences != null && preferences.contains(AppConfig.USER)) {
            String json = preferences.getString(AppConfig.USER, "");
            user = gson.fromJson(json, User.class);
        }
        return user;
    }

}
