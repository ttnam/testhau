package com.yosta.phuotngay.models.user;

import android.support.annotation.RequiresPermission;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @RequiresPermission
    @SerializedName(value = "user_id")
    private String mUserID = null;

    @SerializedName(value = "token")
    private String mToken = null;

    @SerializedName(value = "user_name")
    private String mUserName = null;

    @SerializedName(value = "cover")
    private String mCoverUrl = null;

    @SerializedName(value = "avatar")
    private String mAvatarUrl = null;

    @SerializedName(value = "email")
    private String mEmail = null;

    @SerializedName(value = "gender")
    private int mGender = -1;

    @SerializedName(value = "member_ship")
    private String mMembership = null;

    public User() {
    }

    public String getUserId() {
        return mUserID;
    }

    public void setUserId(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(String mCoverUrl) {
        this.mCoverUrl = mCoverUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int mGender) {
        this.mGender = mGender;
    }

    public String getMembership() {
        return mMembership;
    }

    public void setMembership(String mMembership) {
        this.mMembership = mMembership;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", mUserID, mEmail);
    }
}
