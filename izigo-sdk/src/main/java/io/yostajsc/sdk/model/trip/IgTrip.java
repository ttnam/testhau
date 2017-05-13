package io.yostajsc.sdk.model.trip;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class IgTrip implements IgTripModel, Serializable {

    public static final String TRIP_ID = "mId";

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("cover")
    private String mCover;

    @SerializedName("arrive")
    private IgPlace mArrive;

    @SerializedName("depart")
    private IgPlace mDepart;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("from")
    private IgBaseUserInfo mCreator;

    @SerializedName("album")
    private List<IgImage> mAlbum;

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

    public List<IgImage> getAlbum() {
        return this.mAlbum;
    }

    public long getArriveTime() {
        return mArrive.getTime();
    }

    public long getDepartTime() {
        return mDepart.getTime();
    }

    public String getDescription() {
        if (mDescription == null)
            return "";
        return mDescription;
    }

    public String getFrom() {
        return this.mDepart.getName();
    }

    public String getTo() {
        return this.mArrive.getName();
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

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getCoverUrl() {
        return mCover;
    }

    @Override
    public int getNumberOfViews() {
        return mNumberOfView;
    }

    @Override
    public long getDurationTimeInMillis() {
        return getArriveTime() - getDepartTime();
    }

    public int getStatus() {
        return mStatus;
    }

}
