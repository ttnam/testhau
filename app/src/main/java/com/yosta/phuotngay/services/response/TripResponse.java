package com.yosta.phuotngay.services.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.trip.Trip;

/**
 * Created by Phuc-Hau Nguyen on 2/26/2017.
 */

public class TripResponse extends BaseResponse {

    @SerializedName("data")
    private Trip trip;

    public Trip getTrip() {
        return trip;
    }
}
