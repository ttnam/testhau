package com.yosta.phuotngay.models.trip;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class BaseTrip implements Serializable {

    public static final String EXTRA_TRIP = "EXTRA_TRIP";

    private String id;

    private String arrive;
    private String depart;
    private String name;
    private String cover;
    private int ranking;
    private String createdtime;

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    public String getArrive() {
        return arrive;
    }

}
