package com.yosta.phuotngay.services.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.base.Locations;
import com.yosta.phuotngay.models.trip.BaseTrip;

import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class TripsResponse extends BaseResponse {

    @SerializedName("data")
    private List<BaseTrip> trips = null;

    public List<BaseTrip> getTrips() {
        return trips;
    }

}
