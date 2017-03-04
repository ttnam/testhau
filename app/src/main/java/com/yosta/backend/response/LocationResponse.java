package com.yosta.backend.response;

import com.google.gson.annotations.SerializedName;
import com.yosta.phuotngay.models.base.Locations;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class LocationResponse extends BaseResponse {

    @SerializedName("data")
    private Locations locations = null;

    public Locations getLocations() {
        return locations;
    }
}
