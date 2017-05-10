package io.yostajsc.izigo.usecase.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by nphau on 4/12/17.
 */


public class Person implements ClusterItem {

    private String mId;
    private LatLng mPosition;
    private String mName;
    private String mAvatar;
    private boolean mIsVisible;
    private long mUpdateAt;
    private String distance;
    private String time;

    public Person() {

    }

    public Person(String id, String name, String avatar) {
        this.mId = id;
        this.mName = name;
        this.mAvatar = avatar;
    }

    @Override
    public String getTitle() {
        return getId();
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getAvatar() {
        return this.mAvatar;
    }

    public void setLatLng(LatLng latLng) {
        this.mPosition = latLng;
    }

    public void setVisible(boolean isVisible) {
        this.mIsVisible = isVisible;
    }

    public void setUpdateAt(long updateAt) {
        this.mUpdateAt = updateAt;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isVisible() {
        return mIsVisible;
    }
}
