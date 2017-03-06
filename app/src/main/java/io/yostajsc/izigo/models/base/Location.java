package io.yostajsc.izigo.models.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class Location extends RealmObject implements Serializable {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("name")
    private String name;
}
