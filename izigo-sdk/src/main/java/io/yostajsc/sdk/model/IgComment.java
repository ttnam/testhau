package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

import io.realm.annotations.PrimaryKey;
import io.yostajsc.sdk.model.trip.IgBaseUserInfo;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class IgComment implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("from")
    private IgBaseUserInfo mCreator;

    public IgComment(String mContent) {
        this.mContent = mContent;
        this.mCreator = new IgBaseUserInfo();
        setId(Calendar.getInstance().getTimeInMillis() + "");
    }

    public String getCreatedTime() {
        return this.mId;
    }

    public String getCreatorName() {
        if (this.mCreator == null)
            return "";
        return this.mCreator.getName();
    }

    public String getCreatorAvatar() {
        if (this.mCreator == null)
            return "";
        return this.mCreator.getAvatar();
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getContent() {
        return this.mContent;
    }

    public void setName(String name) {
        mCreator.setName(name);
    }

    public void setAvatar(String avatar) {
        mCreator.setAvatar(avatar);
    }
}
