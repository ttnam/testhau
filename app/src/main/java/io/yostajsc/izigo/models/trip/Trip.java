package io.yostajsc.izigo.models.trip;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.yostajsc.utils.validate.ValidateUtils;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class Trip extends RealmObject implements Serializable {

    public static final String TRIP_ID = "id";

    @PrimaryKey
    private String id;

    private String arrive;
    private String depart;
    private String name;
    private String cover;
    private int ranking;
    private String createdtime;

    @SerializedName("description")
    private String mDescription;

    public String getDescription() {
        return mDescription;
    }

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    public String getTripId() {
        return id;
    }

    private long getArriveTime() {
        if (ValidateUtils.canUse(arrive))
            return Long.parseLong(arrive.split("_")[1]);
        return 0;
    }

    public String getArriveId() {
        return arrive.split("_")[0];
    }

    private long getDepartTime() {
        if (ValidateUtils.canUse(depart))
            return Long.parseLong(depart.split("_")[1]);
        return 0;
    }

    public String getDepartId() {
        return depart.split("_")[0];
    }

    public long getDuration() {
        return getDepartTime() - getArriveTime();
    }
}
