package io.yostajsc.izigo.models.comment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.yostajsc.izigo.models.user.BaseUserInfo;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Comment extends RealmObject implements Serializable {


    @PrimaryKey
    @SerializedName("createdTime")
    private String mId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("from")
    private BaseUserInfo mCreator;


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
