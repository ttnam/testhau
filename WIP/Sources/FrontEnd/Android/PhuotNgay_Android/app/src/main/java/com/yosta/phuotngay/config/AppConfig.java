package com.yosta.phuotngay.config;

import android.app.Application;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yosta.phuotngay.helpers.SharedPresUtils;
import com.yosta.phuotngay.models.user.User;

/**
 * Created by Phuc-Hau Nguyen on 9/13/2016.
 */
public class AppConfig extends Application {

    private User mUser = null;
    private SharedPresUtils mPresUtils = null;

    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * (excluding content providers) have been created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        this.mPresUtils = new SharedPresUtils(this);
        this.mUser = new User();
        this.mUser.setName(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_NAME));
        this.mUser.setUserId(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_ID));
        this.mUser.setGender(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_GENDER));
        this.mUser.setLink(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_LINK_PROFILE));
        this.mUser.setCoverUrl(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_COVER_URL));
        this.mUser.setAvatarUrl(mPresUtils.getSettingString(SharedPresUtils.KEY_USER_AVATAR_URL));
    }

    /**
     * This method is for use in emulated process environments.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public boolean IsUserLogin() {
        String token = getCurrentUserToken();
        return (token != null && !TextUtils.isEmpty(token));
    }

    public void setCurrentUserId(String userId) {
        this.mUser.setUserId(userId);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_ID, userId);
    }

    public boolean setCurrentUserToken(String token) {
        return mPresUtils.saveSetting(SharedPresUtils.KEY_USER_TOKEN, token);
    }

    public String getCurrentUserToken() {
        return mPresUtils.getSettingString(SharedPresUtils.KEY_USER_TOKEN);
    }

    public void setCurrentUserCoverUrl(String coverUrl) {
        this.mUser.setCoverUrl(coverUrl);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_COVER_URL, coverUrl);
    }

    public void setCurrentUserAvatarUrl(String avatarUrl) {
        this.mUser.setAvatarUrl(avatarUrl);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_AVATAR_URL, avatarUrl);
    }

    public void setCurrentUserLink(String link) {
        this.mUser.setLink(link);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_LINK_PROFILE, link);
    }

    public void setCurrentUserEmail(String email) {
        this.mUser.setEmail(email);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_EMAIL, email);
    }

    public void setCurrentUserGender(String gender) {
        this.mUser.setGender(gender);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_GENDER, gender);
    }

    public void setCurrentUserName(String name) {
        this.mUser.setName(name);
        mPresUtils.saveSetting(SharedPresUtils.KEY_USER_NAME, name);
    }

    public User getCurrentUser() {
        return mUser;
    }

    public boolean userLogout() {
        return mPresUtils.removeSettings(SharedPresUtils.KEY_USER_TOKEN);
    }

}
