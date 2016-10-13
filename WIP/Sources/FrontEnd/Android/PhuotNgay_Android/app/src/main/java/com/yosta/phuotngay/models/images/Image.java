package com.yosta.phuotngay.models.images;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Image implements Serializable {

    @SerializedName(value = "imageId")
    private String mImageId;

    @SerializedName(value = "url")
    private String mUrl;


    public Image(String mImageId, String mUrl) {
        this.mImageId = mImageId;
        this.mUrl = mUrl;
    }

}
