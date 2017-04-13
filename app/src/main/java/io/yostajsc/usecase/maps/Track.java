package io.yostajsc.usecase.maps;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by nphau on 4/11/17.
 */

public class Track implements Serializable {

    private long mUpdateAt;
    private LatLng mLatLng;
    private boolean mIsVisible;

    public Track() {
    }

    public long getUpdateAt() {
        return mUpdateAt;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public Track setUpdateAt(long mUpdateAt) {
        this.mUpdateAt = mUpdateAt;
        return this;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public Track setLatLng(LatLng latLng) {
        this.mLatLng = latLng;
        return this;
    }

    public Track setVisible(boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
        return this;
    }
}
