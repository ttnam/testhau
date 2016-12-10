package com.yosta.phuotngay.configs;

import android.app.Application;

import com.yosta.phuotngay.firebase.model.FirebaseUser;
import com.yosta.phuotngay.helpers.app.StorageUtils;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    private static FirebaseUser mUser = null;
    private StorageUtils storageUtils = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.storageUtils = new StorageUtils(this);
        mUser = this.storageUtils.getUser();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public void setUserId(String userId) {
        mUser.setUserID(userId);
    }

    public void setEmail(String email) {
        mUser.setEmail(email);
    }

    public void setUserName(String userName) {
        mUser.setUsername(userName);
    }

    public void saveUser() {
        this.storageUtils.setUser(mUser);
    }
}
