package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.yostajsc.sdk.model.trip.BaseTripInfo;
import io.yostajsc.sdk.model.trip.IgBaseUserInfo;

/**
 * Created by nphau on 3/19/17.
 */

public class IgNotification implements Serializable {

    @SerializedName("content")
    private NotificationContent mContent;

    @SerializedName("type")
    private int mType;

    @SerializedName("id")
    private String mId;

    public IgBaseUserInfo getFrom() {
        return mContent.getFrom();
    }

    public BaseTripInfo getTrip() {
        return mContent.getTrip();
    }

    public int getType() {
        return mType;
    }

    public String getId() {
        return mId;
    }
}
