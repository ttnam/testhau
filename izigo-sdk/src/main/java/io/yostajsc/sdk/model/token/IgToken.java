package io.yostajsc.sdk.model.token;

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

    public boolean isExpired() {
        return new Date().after(new Date(mExpired));
    }

    public String getToken() {
        return mToken;
    }

    public String getFbId() {
        return mFbId;
    }

    public void setFbId(String mFbId) {
        this.mFbId = mFbId;
    }
}
