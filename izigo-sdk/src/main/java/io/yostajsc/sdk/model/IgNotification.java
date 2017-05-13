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
    private Content mContent;

    @SerializedName("type")
    private int mType;

    @SerializedName("id")
    private String mId;

    public IgBaseUserInfo getFrom() {
        if (mContent == null)
            return null;
        return mContent.getFrom();
    }

    public BaseTripInfo getTrip() {
        if (mContent == null)
            return null;
        return mContent.getTrip();
    }

    public int getType() {
        return mType;
    }

    public String getId() {
        return mId;
    }

    public class Content implements Serializable {

        @SerializedName("from")
        private IgBaseUserInfo from;

        @SerializedName("trip")
        private BaseTripInfo trip;

        @SerializedName("message")
        private String mMessage;

        public IgBaseUserInfo getFrom() {
            return from;
        }

        public BaseTripInfo getTrip() {
            return trip;
        }
    }

    public String getMessage() {
        return mContent.mMessage;
    }
}
