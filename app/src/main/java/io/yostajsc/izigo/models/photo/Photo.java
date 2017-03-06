package io.yostajsc.izigo.models.photo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Photo implements Serializable {

    @SerializedName(value = "photo_id")
    private String mPhotoId;

    @SerializedName(value = "images")
    private String mImages;

    @SerializedName(value = "link")
    private String mUrl;

    @SerializedName(value = "from")
    private String mUserId;

    @SerializedName(value = "place")
    private String mPlaceId;


    public Photo() {
    }

    public Photo(String mPhotoId, String mImages, String mUrl, String mUserId, String mPlaceId) {
        this.mPhotoId = mPhotoId;
        this.mImages = mImages;
        this.mUrl = mUrl;
        this.mUserId = mUserId;
        this.mPlaceId = mPlaceId;
    }
}
