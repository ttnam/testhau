package com.yosta.phuotngay.models.followers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Follower implements Serializable {

    @SerializedName(value = "userId")
    private String mUserId;
    @SerializedName(value = "userName")
    private String mUserName;
    @SerializedName(value = "avatar")
    private String mAvatar;

    public Follower(String mUserId, String mUserName, String mAvatar) {
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mAvatar = mAvatar;
    }
}
