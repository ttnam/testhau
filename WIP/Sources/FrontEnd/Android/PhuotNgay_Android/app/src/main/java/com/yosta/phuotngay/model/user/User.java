package com.yosta.phuotngay.model.user;

import android.support.annotation.RequiresPermission;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    public static String permission = "email,name,gender,cover,picture.type(large),link";

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
    private String mGender = null;

    @SerializedName(value = "followers")
    private List<User> mFollowers = null;

    @SerializedName(value = "followings")
    private List<User> mFollowings = null;

    @SerializedName(value = "is_active")
    private boolean mIsActive = false;

    private String mMembership = null;


    public void setUserId(String userId) {
        this.mUserID = userId;
    }

    public void setCoverUrl(String coverUrl) {
        this.mCoverUrl = coverUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
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
