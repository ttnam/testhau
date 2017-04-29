package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class Timeline extends RealmObject implements Serializable {


    @PrimaryKey
    @SerializedName("time")
    private String mTime;

    @SerializedName("content")
    private String mContent;


    public String getContent() {
        return mContent;
    }

    public long getTime() {
        if (mTime == null)
            return 0;
        return Long.parseLong(mTime);
    }

}
