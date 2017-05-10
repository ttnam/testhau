package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.yostajsc.sdk.model.trip.BaseTripInfo;
import io.yostajsc.sdk.model.trip.IgBaseUserInfo;
import io.yostajsc.sdk.model.trip.IgTrip;

/**
 * Created by nphau on 3/20/17.
 */

public class NotificationContent implements Serializable {

    @SerializedName("from")
    private IgBaseUserInfo from;


    @SerializedName("trip")
    private BaseTripInfo trip;


    public IgBaseUserInfo getFrom() {
        return from;
    }

    public BaseTripInfo getTrip() {
        return trip;
    }
}
