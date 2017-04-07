package io.yostajsc.realm.trip;

import android.util.SparseArray;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.yostajsc.izigo.interfaces.TripModel;
import io.yostajsc.izigo.models.trip.LocationPick;

/**
 * Created by nphau on 4/7/17.
 */

public class PublicTrip extends RealmObject implements Serializable, TripModel {

    public static final String KEY = "mId";

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("cover")
    private String mCover;

    @SerializedName("arrive")
    private LocationPick mArrive;

    @SerializedName("depart")
    private LocationPick mDepart;

    @SerializedName("numberOfView")
    private int mNumberOfView;

    @Override
    public String getId() {
        return this.mId;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    public String getCoverUrl() {
        return this.mCover;
    }

    @Override
    public int getNumberOfViews() {
        return this.mNumberOfView;
    }

    private long getArriveTime() {
        return mArrive.getTime();
    }

    private long getDepartTime() {
        return mDepart.getTime();
    }

    @Override
    public long getDurationTimeInMillis() {
        return getArriveTime() - getDepartTime();
    }
}
