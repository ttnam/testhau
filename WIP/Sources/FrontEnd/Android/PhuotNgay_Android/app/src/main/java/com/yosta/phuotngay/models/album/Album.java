package com.yosta.phuotngay.models.album;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.photo.Photos;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Album implements Serializable {

    @SerializedName(value = "album_id")
    private String mAlbumId;

    @SerializedName(value = "count")
    private int mCount;

    @SerializedName(value = "cover_photo")
    private int mCoverId;

    @SerializedName(value = "from")
    private String mUserId;

    @SerializedName(value = "place")
    private String mPlaceId;

    @SerializedName(value = "photos")
    private Photos mPhotos;

    public Album() {
    }

}
