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
}
