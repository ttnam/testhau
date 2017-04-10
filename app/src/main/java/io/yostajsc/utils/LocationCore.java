package io.yostajsc.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.yostajsc.core.code.MessageType;

/**
 * Created by Phuc-Hau Nguyen on 12/27/2016.
 */

public class LocationCore implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String TAG = LocationCore.class.getSimpleName();

    private static final double RADIUS_OF_EARTH_METERS = 6371009.0;
    private static final int TIME = 1000 * 60;

    private static volatile LocationCore defaultInstance;

    private GoogleApiClient mGoogleApiClient = null;

    private LocationCore() {

    }

    private LocationCore(final Activity activity) {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

            onAskGps(activity);
        }
    }

    public static LocationCore make() {
        if (defaultInstance == null) {
            synchronized (LocationCore.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LocationCore();
                }
            }
        }
        return defaultInstance;
    }

    public static LocationCore askGps(Activity activity) {
        if (defaultInstance == null) {
            synchronized (LocationCore.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LocationCore(activity);
                }
            }
        }
        return defaultInstance;
    }

    private void onAskGps(final Activity activity) {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final int statusCode = status.getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, MessageType.USER_GPS);
                        } catch (Exception e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public static boolean isEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isBetter(Location newLocation, Location current) {
        // A new location is always better than no location
        if (current == null) {
            return true;
        }
        if (newLocation.distanceTo(current) > 50) {
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - current.getTime();
        return (timeDelta > TIME);
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(
                radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(
                center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    public static LatLng populateLocalStationParams(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        return new LatLng(0, 0);
    }

    public static void addShowCustomMarker(final GoogleMap map, final LatLng latLng) {
        map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        onMoveCameraSmoothly(map, latLng);
    }

    public static void onMoveCameraSmoothly(final GoogleMap map, final LatLng latLng) {

        map.moveCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(latLng, 13f, 0, 0)));

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                map.animateCamera(CameraUpdateFactory.scrollBy(
                        (float) latLng.latitude, (float) latLng.longitude),
                        3000, null);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
