package com.yosta.phuotngay.models.trip;

import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.helpers.validate.ValidateHelper;

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

    public String getTripId() {
        return id;
    }

    public long getArriveTime() {
        if (ValidateHelper.canUse(arrive))
            return Long.parseLong(arrive.split("_")[1]);
        return 0;
    }

    public String getArriveId() {
        return arrive.split("_")[0];
    }

    public long getDepartTime() {
        if (ValidateHelper.canUse(depart))
            return Long.parseLong(depart.split("_")[1]);
        return 0;
    }

    public String getDepartId() {
        return depart.split("_")[0];
    }

    public long getDuration() {
        long gap = getDepartTime() - getArriveTime();
        return gap;
    }
}
