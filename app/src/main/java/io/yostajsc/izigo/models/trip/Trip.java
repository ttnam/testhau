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

    @SerializedName("description")
    private String mDescription;

    @SerializedName("from")
    private BaseUserInfo mCreator;

    @SerializedName("album")
    private RealmList<BasePhotoInfo> mAlbum;
    @SerializedName("is_published")
    private String mIsPublished;

    @SerializedName("role")
    private String mRole;

    @SerializedName("numberOfActivities")
    private String mNumberOfActivities;

    @SerializedName("numberOfComments")
    private String mNumberOfComments;

    @SerializedName("numberOfMembers")
    private String mNumberOfMembers;

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
        return getDepartTime() - getArriveTime();
    }

    public String getDescription() {
        if (mDescription == null)
            return "";
        return mDescription;
    }

    public int getNumberOfActivities() {
        int n = 0;
        try {
            n = Integer.parseInt(mNumberOfActivities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    public int getNumberOfComments() {
        int n = 0;
        try {
            n = Integer.parseInt(mNumberOfComments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    public int getNumberOfMembers() {
        int n = 0;
        try {
            n = Integer.parseInt(mNumberOfMembers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }


    public int getNumberOfView() {
        int n = 0;
        try {
            n = Integer.parseInt(mNumberOfView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }
}
