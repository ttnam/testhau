package io.yostajsc.izigo.models.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.yostajsc.izigo.models.user.BaseUserInfo;

/**
 * Created by nphau on 3/19/17.
 */

public class Notification extends RealmObject implements Serializable {

    @SerializedName("content")
    private BaseUserInfo mContent;

    @SerializedName("type")
    private int mType;

    @SerializedName("id")
    private String mTripId;


}
