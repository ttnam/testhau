package com.yosta.phuotngay.models.location;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class FirebaseLocation implements Serializable {
    private double lat;
    private double lng;
    private String name;
    private long time;

    public FirebaseLocation() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
