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

    @SerializedName("status")
    private String mStatus;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("from")
    private BaseUserInfo mCreator;

    @SerializedName("album")
    private RealmList<BasePhotoInfo> mAlbum;

    @SerializedName("is_published")
    private String mIsPublished;

    @SerializedName("role")
    private int mRole;

    @SerializedName("numberOfView")
    private int mNumberOfView;

    @SerializedName("numberOfActivities")
    private int mNumberOfActivities;

    @SerializedName("numberOfComments")
    private int mNumberOfComments;

    @SerializedName("numberOfMembers")
    private int mNumberOfMembers;


    @SerializedName("transfer")
    private int mTransfer;


    public String getTripName() {
        return mName;
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

    public RealmList<BasePhotoInfo> getAlbum() {
        return this.mAlbum;
    }

    public String getCover() {
        return mCover;
    }

    public String getTripId() {
        return mId;
    }

    public long getArriveTime() {
        return mArrive.getTime();
    }

    public long getDepartTime() {
        return mDepart.getTime();
    }

    public long getDuration() {
        return getArriveTime() - getDepartTime();
    }

    public String getDescription() {
        if (mDescription == null)
            return "";
        return mDescription;
    }

    public int getNumberOfActivities() {
        return this.mNumberOfActivities;
    }

    public int getNumberOfComments() {
        return this.mNumberOfComments;
    }

    public int getNumberOfMembers() {
        return this.mNumberOfMembers;
    }


    public int getNumberOfView() {
        return this.mNumberOfView;
    }

    public int getTransfer() {
        return mTransfer;
    }

    public int getRole() {
        return mRole;
    }

    public boolean isPublished() {
        return this.mIsPublished.equals("1");
    }
}
