package com.yosta.phuotngay.model.album;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.model.photo.Photos;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Album implements Serializable {

    @SerializedName(value = "album_id")
    private String mAlbumId = null;

    @SerializedName(value = "count")
    private int mCount = 0;

    @SerializedName(value = "cover_photo")
    private int mCoverId = -1;

    @SerializedName(value = "from")
    private String mUserId = null;

    @SerializedName(value = "place")
    private String mPlaceId = null;

    @SerializedName(value = "photos")
    private Photos mPhotos = null;

    public Album() {
        this.mAlbumId = null;
        this.mUserId = null;
        this.mPlaceId = null;
        this.mCount = 0;
        this.mCoverId = -1;
        this.mPhotos = new Photos();
    }

}
