package com.yosta.phuotngay.firebase.model;

import android.support.annotation.RequiresPermission;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FirebaseUser implements Serializable {

    @RequiresPermission
    @SerializedName(value = "user_id")
    private String mUserID = null;

    @SerializedName(value = "token")
    private String token = null;

    private String username = null;
    private String cover = null;
    private String avatar = null;
    private String email = null;
    private long gender = -1;
    private long membership = 0;

    private BaseUserInfo followers;
    private BaseUserInfo followings;

    public FirebaseUser() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getMembership() {
        return membership;
    }

    public void setMembership(long membership) {
        this.membership = membership;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getGender() {
        return gender;
    }

    public void setGender(long gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", mUserID, username, email);
    }

    public class BaseUserInfo {
        private String userid;
    }
}
