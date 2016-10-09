package com.yosta.phuotngay.models.user;

import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.yosta.phuotngay.helpers.SharedPresUtils;
import com.yosta.phuotngay.helpers.listeners.ListenerHelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

    public static String permission = "email,name,gender,cover,picture.type(large),link";

    @RequiresPermission
    private String mUserID = null;
    private String mFullName = null;
    private String mEmail = null;
    private String mCoverUrl = null;
    private String mAvatarUrl = null;
    private String mAccessToken = null;
    private String mLink = null;
    private String mGender = null;

    public void setUserId(String userId) {
        this.mUserID = userId;
    }

    public void setCoverUrl(String coverUrl) {
        this.mCoverUrl = coverUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public boolean IsValid() {
        return (this.mUserID != null && this.mCoverUrl != null &&
                this.mAvatarUrl != null && this.mAccessToken != null &&
                !TextUtils.isEmpty(this.mUserID) && !TextUtils.isEmpty(this.mCoverUrl)
                && !TextUtils.isEmpty(this.mAvatarUrl) && !TextUtils.isEmpty(this.mAccessToken));
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public void setToken(String token) {
        this.mAccessToken = token;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public void setName(String name) {
        this.mFullName = name;
    }

    public String getCoverUrl() {
        return this.mCoverUrl;
    }

    public String getAvatarUrl() {
        return this.mAvatarUrl;
    }

    public String getUserName() {
        return this.mFullName;
    }

    public String getGender() {
        return this.mGender;
    }
}
