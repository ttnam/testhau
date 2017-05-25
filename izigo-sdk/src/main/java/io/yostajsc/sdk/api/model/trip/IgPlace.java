package io.yostajsc.sdk.api.model.trip;

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

    private int mYYYY = -1, mMM = -1, mDD = -1, mH = -1, mM = -1;

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

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLatLng(double lat, double lng) {
        setLat(lat);
        setLng(lng);
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

    public void setYear(int year) {
        mYYYY = year;
    }

    public void setDate(int year, int month, int day) {
        setDay(day);
        setMonth(month);
        setYear(year);
    }

    public void setTime(int hour, int minute) {
        setHour(hour);
        setMinute(minute);
    }

    public void setMonth(int month) {
        mMM = month;
    }

    public void setDay(int day) {
        mDD = day;
    }

    public void setHour(int hour) {
        mH = hour;
    }

    public void setMinute(int minute) {
        mM = minute;
    }

    public long getTime() {
        if (mYYYY != -1 && mMM != -1 && mDD != -1 && mM != -1) {
            setTime(mYYYY, mMM, mDD, mH, mM);
        }
        if (time <= 0)
            return Calendar.getInstance().getTimeInMillis();
        return time;
    }

    @Override
    public String toString() {
        return lat + ";" + lng + ";" + name + ";" + time;
    }

}
