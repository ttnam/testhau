package io.yostajsc.core.realm.trip;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class IgTrip implements TripModel, Serializable {

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
    private List<Image> mAlbum;

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

    public List<Image> getAlbum() {
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

    public class BaseUserInfo implements Serializable {

        @SerializedName("avatar")
        private String avatar;

        @SerializedName("fbId")
        private String fbId;

        @SerializedName("name")
        private String name;

        public String getAvatar() {
            return avatar;
        }

        public String getName() {
            return name;
        }
    }

    public class Image implements Serializable {

        @PrimaryKey
        @SerializedName("id")
        private String mId;

        @SerializedName("url")
        private String mUrl;

        public String getUrl() {
            return this.mUrl;
        }
    }

}
