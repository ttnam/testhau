package io.yostajsc.izigo.models.trip;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Phuc-Hau Nguyen on 3/9/2017.
 */

public class LocationPick extends RealmObject implements Serializable {


    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("name")
    private String name;

    @SerializedName("time")
    private long time;

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return lat + ";" + lng + ";" + name + ";" + time;
    }
}
