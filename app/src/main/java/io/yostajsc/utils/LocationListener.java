package io.yostajsc.utils;

import android.location.Location;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.AppConfig;

/**
 * Created by nphau on 3/21/17.
 */

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = LocationListener.class.getSimpleName();

    private DatabaseReference mDatabase;
    private Location mLastLocation;

    public LocationListener(String provider) {
        mLastLocation = new Location(provider);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onLocationChanged(final Location location) {

        if (LocationCore.make().isBetter(location, mLastLocation)) {

            mLastLocation = location;

            final String tripId = StorageUtils.inject(AppConfig.getInstance()).getString(AppConfig.TRIP_ID);
            final String fbId = StorageUtils.inject(AppConfig.getInstance()).getString(AppConfig.FB_ID);

            mDatabase.child("TRACK/" + tripId + "/" + fbId + "/geo/" + Calendar.getInstance().getTimeInMillis())
                    .setValue(
                            location.getLatitude() + ", " +
                                    location.getLongitude() + ", 1");
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