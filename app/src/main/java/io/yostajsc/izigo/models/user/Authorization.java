package io.yostajsc.izigo.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nphau on 4/12/17.
 */

public class Authorization implements Serializable {

    @SerializedName("token")
    private String mToken;

    @SerializedName("expired")
    private long mExpired;


    public boolean isExpired() {
        return new Date().after(new Date(mExpired));
    }

    public String getToken() {
        return mToken;
    }
}
