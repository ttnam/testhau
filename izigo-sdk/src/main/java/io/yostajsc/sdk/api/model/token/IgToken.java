package io.yostajsc.sdk.api.model.token;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nphau on 4/12/17.
 */

public class IgToken implements Serializable {

    @SerializedName("token")
    private String mToken;

    @SerializedName("expired")
    private long mExpired;

    private String mFbId;
    private String mAvatar;
    private String mName;

    public boolean isExpired() {
        return new Date().after(new Date(mExpired));
    }

    public String getToken() {
        return mToken;
    }

    public String getFbId() {
        return mFbId;
    }

    public void setFbId(String fbId) {
        this.mFbId = fbId;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
