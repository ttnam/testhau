package com.yosta.phuotngay.model.place;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.model.album.Albums;
import com.yosta.phuotngay.model.comment.Comments;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/13/2016.
 */

public class Place implements Serializable {

    @SerializedName(value = "place_id")
    private String mPlaceId = null;;

    @SerializedName(value = "name")
    private String mName = null;

    @SerializedName(value = "location_name")
    private String mLocationName = null;

    @SerializedName(value = "position")
    private float[] mPosition = null;

    @SerializedName(value = "rating")
    private int mRating = 0;

    @SerializedName(value = "cover")
    private String mCover = null;

    @SerializedName(value = "content")
    private String mContent = null;

    @SerializedName(value = "albums")
    private Albums mAlbumIds = null;

    @SerializedName(value = "comments")
    private Comments mCommentsId = null;

    @SerializedName(value = "from")
    private String mUserId = null;

    @SerializedName(value = "is_active")
    private boolean mIsActive = false;

    public Place() {
        this.mIsActive = false;

    }

    public String getName() {
        return mName;
    }
}
