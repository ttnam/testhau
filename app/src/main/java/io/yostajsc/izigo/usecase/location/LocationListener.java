package io.yostajsc.izigo.usecase.location;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.sdk.api.IzigoSdk;

/**
 * Created by nphau on 3/21/17.
 */

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = LocationListener.class.getSimpleName();


    private DatabaseReference mDatabase;
    private Location mLastLocation;

    private String mTripId = null, mOwnFbId = null;

    public LocationListener(String provider) {
        mLastLocation = new Location(provider);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onLocationChanged(final Location location) {

        if (MapUtils.Map.isBetter(location, mLastLocation)) {

            mLastLocation = location;

            mTripId = AppConfig.getInstance().getCurrentTripId();
            if (TextUtils.isEmpty(mTripId))
                return;

            mOwnFbId = IzigoSdk.UserExecutor.getOwnFbId();
            if (TextUtils.isEmpty(mOwnFbId))
                return;


            mDatabase.child("TRACK/" + mTripId + "/" + mOwnFbId +
                    "/geo/" + Calendar.getInstance().getTimeInMillis())
                    .setValue(
                            location.getLatitude() + ", " +
                                    location.getLongitude() + ", 1");

            Log.e(TAG, mLastLocation.toString());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}