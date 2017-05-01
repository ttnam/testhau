package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;
import io.yostajsc.sdk.model.trip.IgTrip;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Comment implements Serializable {

    @PrimaryKey
    @SerializedName("createdTime")
    private String mId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("from")
    private IgTrip.BaseUserInfo mCreator;


    public long getCreatedTime() {
        return Long.parseLong(mId);
    }

    public String getCreatorName() {
        if (mCreator == null)
            return "";
        return mCreator.getName();
    }

    public String getCreatorAvatar() {
        if (mCreator == null)
            return "";
        return mCreator.getAvatar();
    }

    public String getContent() {
        return mContent;
    }

}
