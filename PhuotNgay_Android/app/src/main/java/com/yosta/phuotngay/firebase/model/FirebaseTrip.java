package com.yosta.phuotngay.firebase.model;

import com.yosta.phuotngay.helpers.app.DatetimeUtils;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTrip implements Serializable {

    private String arrive;
    private String depart;
    private String cover;
    private long createdtime;
    private String description;
    private String from;
    private String name;
    private long rating;

    public FirebaseTrip() {
    }

    public String[] getArrive() {
        return arrive.split("_");
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreatedtime() {
        return DatetimeUtils.getTime(DatetimeUtils.timeStampToDate(createdtime));
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String[] getDepart() {
        return arrive.split("_");
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }
}
