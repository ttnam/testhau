package com.yosta.phuotngay.configs;

import android.app.Application;
import android.content.res.Configuration;

import com.yosta.phuotngay.helpers.app.StorageUtils;
import com.yosta.phuotngay.models.user.User;

/**
 * Created by Phuc-Hau Nguyen on 11/9/2016.
 */

public class AppConfig extends Application {

    private static User mUser = null;
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

    public User getUser() {
        return mUser;
    }

    public void setUserId(String userId) {
        mUser.setUserId(userId);
    }

    public void setEmail(String email) {
        mUser.setEmail(email);
    }

    public void setUserName(String userName) {
        mUser.setUserName(userName);
    }

    public void setCoverUrl(String coverUrl) {
        mUser.setCoverUrl(coverUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        mUser.setAvatarUrl(avatarUrl);
    }

    public void setMembership(String userId) {
        mUser.setMembership(userId);
    }

    public void setGender(int gender) {
        mUser.setGender(gender);
    }

    public void saveUser() {
        this.storageUtils.setUser(mUser);
    }
}