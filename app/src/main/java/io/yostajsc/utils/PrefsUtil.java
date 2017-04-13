package io.yostajsc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.models.user.Authorization;

/**
 * Created by nphau on 4/13/17.
 */

public class PrefsUtil extends StorageUtils {

    protected static PrefsUtil mInstance =  null;

    protected PrefsUtil(Context context) {
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

}
