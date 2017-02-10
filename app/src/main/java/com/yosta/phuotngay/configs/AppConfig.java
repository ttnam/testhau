package com.yosta.phuotngay.configs;

import android.app.Application;

import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.AppHelper;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    private static User mUser = null;
    //private StorageHelper storageHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // this.storageHelper = new StorageHelper(this);
        //mUser = this.storageHelper.getUser();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /*public User getUser() {
        return mUser;
    }*/
}
