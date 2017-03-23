package io.yostajsc.izigo.firebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import io.yostajsc.core.utils.AppUtils;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
@IgnoreExtraProperties
public class FirebaseGroup implements Serializable {

    private String tripId;
    private String arrive;
    private String depart;
    private String cover;
    private long createdtime;
    private String description;
    private String from;
    private String name;
    private long ranking;

    public FirebaseGroup() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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

    public long getRanking() {
        return ranking;
    }

    public void setRanking(long rating) {
        this.ranking = rating;
    }

    public String[] getDepart() {
        return arrive.split("_");
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
