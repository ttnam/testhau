package io.yostajsc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.models.user.Authorization;
import io.yostajsc.izigo.models.user.User;

/**
 * Created by nphau on 4/13/17.
 */

public class PrefsUtil extends StorageUtils {

    private static PrefsUtil mInstance =  null;

    private PrefsUtil(Context context) {
        super(context);
    }

    public static PrefsUtil inject(Context context) {
        if (mInstance == null) {
            mInstance = new PrefsUtil(context);
        }
        return mInstance;
    }


    public void updateAuthorization(Authorization authorization) {
        if (preferences == null) return;

        Gson gson = new Gson();
        String json = gson.toJson(authorization);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConfig.USER, json);
        editor.apply();
    }

    public Authorization getAuthorization() {
        Gson gson = new Gson();
        Authorization authorization = null;
        if (preferences != null && preferences.contains(AppConfig.USER)) {
            String json = preferences.getString(AppConfig.USER, "");
            authorization = gson.fromJson(json, Authorization.class);
        }
        return authorization;
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
