package io.yostajsc.sdk.model.trip;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Phuc-Hau Nguyen on 3/9/2017.
 */

public class IgPlace implements Serializable {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("name")
    private String name;

    @SerializedName("time")
    private long time;

    public IgPlace() {

    }

    public IgPlace(int yyyy, int m, int d, int h, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yyyy, m, d, h, min);
        this.time = calendar.getTimeInMillis();
    }

    public IgPlace(String name, int yyyy, int m, int d, int h, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yyyy, m, d, h, min);
        this.time = calendar.getTimeInMillis();
        this.name = name;
    }

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

    public void setTime(int yyyy, int m, int d, int h, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yyyy, m, d, h, min);
        this.time = calendar.getTimeInMillis();
    }

    @Override
    public String toString() {
        return lat + ";" + lng + ";" + name + ";" + time;
    }

}
