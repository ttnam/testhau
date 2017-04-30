package io.yostajsc.sdk.model.trip;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

/**
 * Created by nphau on 4/30/17.
 */

public class IgImage implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("url")
    private String mUrl;

    private Bitmap mBitmap;

    public String getUrl() {
        return this.mUrl;
    }

    public String getId() {
        return mId;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public IgImage(String id, Bitmap bitmap) {
        this.mId = id;
        this.mBitmap = bitmap;
    }
}