package io.yostajsc.izigo.models.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.yostajsc.izigo.models.trip.BaseTripInfo;
import io.yostajsc.izigo.models.user.BaseUserInfo;

/**
 * Created by nphau on 3/20/17.
 */

public class NotificationContent extends RealmObject implements Serializable {

    @SerializedName("from")
    private BaseUserInfo from;


    @SerializedName("trip")
    private BaseTripInfo trip;


    public BaseUserInfo getFrom() {
        return from;
    }

    public BaseTripInfo getTrip() {
        return trip;
    }
}
