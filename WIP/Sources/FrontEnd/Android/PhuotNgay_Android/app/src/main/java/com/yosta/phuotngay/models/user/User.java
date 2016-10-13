package com.yosta.phuotngay.models.user;

import android.support.annotation.RequiresPermission;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    public static String permission = "email,name,gender,cover,picture.type(large),link";

    @RequiresPermission
    @SerializedName(value = "userId")
    private String mUserID = null;

    @SerializedName(value = "userName")
    private String mUserName = null;

    private String mEmail = null;
    private String mCoverUrl = null;

    @SerializedName(value = "avatar")
    private String mAvatarUrl = null;

    private String mLink = null;

    @SerializedName(value = "gender")
    private String mGender = null;

    @SerializedName(value = "token")
    private String mToken = null;

    public void setUserId(String userId) {
        this.mUserID = userId;
    }

    public void setCoverUrl(String coverUrl) {
        this.mCoverUrl = coverUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public void setName(String name) {
        this.mUserName = name;
    }

    public String getCoverUrl() {
        return this.mCoverUrl;
    }

    public String getAvatarUrl() {
        return this.mAvatarUrl;
    }

    public String getName() {
        return this.mUserName;
    }

    public String getGender() {
        return this.mGender;
    }
}
