package com.yosta.phuotngay.firebase.model;

import com.yosta.phuotngay.models.location.FirebaseLocation;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */

public class FirebaseTrip implements Serializable {

    private FirebaseLocation arrive;
    private String cover;
    private long created_time;
    private FirebaseLocation depart;
    private String description;
    private String from; // user_id
    private String name;
    private int rating;

    public FirebaseTrip() {
    }

    public FirebaseLocation getArrive() {
        return arrive;
    }

    public void setArrive(FirebaseLocation arrive) {
        this.arrive = arrive;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public FirebaseLocation getDepart() {
        return depart;
    }

    public void setDepart(FirebaseLocation depart) {
        this.depart = depart;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name;
    }
}
