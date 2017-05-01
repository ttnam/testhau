package io.yostajsc.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.yostajsc.sdk.model.trip.BaseTripInfo;
import io.yostajsc.sdk.model.trip.IgTrip;

/**
 * Created by nphau on 3/20/17.
 */

public class NotificationContent implements Serializable {

    @SerializedName("from")
    private IgTrip.BaseUserInfo from;


    @SerializedName("trip")
    private BaseTripInfo trip;


    public IgTrip.BaseUserInfo getFrom() {
        return from;
    }

    public BaseTripInfo getTrip() {
        return trip;
    }
}
