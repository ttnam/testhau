package io.yostajsc.utils;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by nphau on 3/21/17.
 */

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = LocationListener.class.getSimpleName();

    public LocationListener(String provider) {

    }

    @Override
    public void onLocationChanged(final Location location) {

     /*   APIManager.connect().updateLocation(
                location.getLongitude() + "",
                location.getLatitude() + "",
                DatetimeUtils.getTimeZone(), "android" + Build.VERSION.RELEASE,
                Build.SERIAL, new CallBack() {
                    @Override
                    public void run() {
                        Log.e(TAG, "Success - " + location.toString());
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String error) {
                        Log.e(TAG, error);
                    }
                }, new CallBack() {
                    @Override
                    public void run() {
                        Log.e(TAG, "Expired");
                    }
                });*/

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