package io.yostajsc.izigo.models.trip;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.yostajsc.izigo.models.photo.BasePhotoInfo;
import io.yostajsc.izigo.models.user.BaseUserInfo;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class Trip extends RealmObject implements Serializable {

    public static final String TRIP_ID = "mId";

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("cover")
    private String mCover;

    @SerializedName("arrive")
    private LocationPick mArrive;

    @SerializedName("depart")
    private LocationPick mDepart;

    @SerializedName("numberOfView")
    private String mNumberOfView;

    @SerializedName("status")
    private String mStatus;
/*
    @SerializedName("description")
    private String mDescription;*/
/*

    @SerializedName("from")
    private BaseUserInfo mCreater;

    @SerializedName("album")
    private RealmList<BasePhotoInfo> mAlbum;
*/
/*
    @SerializedName("is_published")
    private String mIsPublished;

    @SerializedName("role")
    private String mRole;*/


    public String getTripName() {
        return mName;
    }

    public String getCreatorName() {
        // return mCreater.getName();
        return "";
    }

    public String getCreatorAvatar() {
        // return mCreater.getAvatar();
        return "";
    }

    public RealmList<BasePhotoInfo> getAlbum() {
        // return this.mAlbum;
        return null;
    }

    public String getCover() {
        return mCover;
    }

    public String getTripId() {
        return mId;
    }

    private long getArriveTime() {
        return mArrive.getTime();
    }

    private long getDepartTime() {
        return mDepart.getTime();
    }

    public long getDuration() {
        return getDepartTime() - getArriveTime();
    }

    public String getDescription() {
        // return mDescription;
        return "";
    }
}
