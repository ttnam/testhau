package io.yostajsc.core;

import android.app.Application;

public class CoreApp extends Application {

    public static final String TAG = CoreApp.class.getSimpleName();

    private static CoreApp instance;
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized CoreApp getInstance() {
        return instance;
    }

}