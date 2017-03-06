package io.yostajsc.izigo.firebase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 12/10/2016.
 */

public class FirebaseTrips implements Serializable {

    List<FirebaseTrip> trips = new ArrayList<>();

    public FirebaseTrips() {
    }

    public FirebaseTrips(List<FirebaseTrip> trips) {
        this.trips = trips;
    }

    public List<FirebaseTrip> getTrips() {
        return trips;
    }

    public void setTrips(List<FirebaseTrip> trips) {
        this.trips = trips;
    }
}
