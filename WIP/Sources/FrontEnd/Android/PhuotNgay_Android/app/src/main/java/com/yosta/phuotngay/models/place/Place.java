package com.yosta.phuotngay.models.place;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.album.Album;
import com.yosta.phuotngay.models.album.Albums;
import com.yosta.phuotngay.models.comment.Comments;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/13/2016.
 */

public class Place implements Serializable {

    @SerializedName(value = "place_id")
    private String mPlaceId;

    @SerializedName(value = "name")
    private String mName;

    @SerializedName(value = "location_name")
    private String mLocationName;

    @SerializedName(value = "position")
    private float[] mPosition;

    @SerializedName(value = "rating")
    private int mRating;

    @SerializedName(value = "cover")
    private String mCover;

    @SerializedName(value = "content")
    private String mContent;

    @SerializedName(value = "albums")
    private Albums mAlbumIds;

    @SerializedName(value = "comments")
    private Comments mCommentsId;

    @SerializedName(value = "from")
    private String mUserId = null;

    @SerializedName(value = "is_active")
    private boolean mIsActive = false;

    public Place() {
    }
}
