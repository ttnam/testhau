package io.yostajsc.izigo.usecase.map.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.consts.MessageType;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Phuc-Hau Nguyen on 12/27/2016.
 */

public class MapUtils {

    private static String TAG = MapUtils.class.getSimpleName();

    private static final String urlGoogleAPI = "https://maps.googleapis.com/maps/api/directions/json?language=vi&";

    private static final int MIN_TIME = 1000 * 60 * 5;
    private static final int MIN_DISTANCE = 100;
    private static final double RADIUS_OF_EARTH_METERS = 6371009.0;

    public static class Gps implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        private Activity mActivity = null;
        private GoogleApiClient mGoogleApiClient = null;

        private static Gps[] mInstance = new Gps[2];

        private Gps() {
        }

        private Gps(Activity activity) {
            this.mActivity = activity;
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(activity)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
            }
        }

        public static Gps request(Activity activity) {
            if (mInstance[0] == null)
                mInstance[0] = new Gps(activity);
            return mInstance[0];
        }

        public static Gps connect() {
            if (mInstance[1] == null)
                mInstance[1] = new Gps();
            return mInstance[1];
        }

        public boolean isEnable() {
            LocationManager locationManager = (LocationManager) AppConfig.getInstance().getApplicationContext().getSystemService(LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        public void askGPS() {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(2 * 1000);

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
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(mActivity, MessageType.USER_GPS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
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

        public void getLastKnownLocation(Context context, OnGetLastKnownLocation callback) {

            try {

                LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                // getting GPS status
                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        callback.onReceive(new LatLng(
                                location.getLatitude(),
                                location.getLongitude())
                        );
                        return;
                    }
                }

                if (isNetworkEnabled) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null)
                        callback.onReceive(new LatLng(
                                location.getLatitude(),
                                location.getLongitude())
                        );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class Map {

        public static void moveCameraSmoothly(final GoogleMap map, final LatLng latLng, final int timeInMills) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,
                    map.getCameraPosition().zoom,
                    map.getCameraPosition().tilt,
                    map.getCameraPosition().bearing)));
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    moveCamera(map, latLng, timeInMills);
                }
            });
        }

        private static void moveCamera(final GoogleMap map, final LatLng latLng, int timeInMills) {
            map.animateCamera(CameraUpdateFactory.scrollBy((float) latLng.latitude, (float) latLng.longitude),
                    timeInMills, null);
        }

        public static Marker addMarker(final GoogleMap map, final LatLng latLng, final String title, final int timeInMills) {
            Marker marker = map.addMarker(
                    new MarkerOptions().position(latLng).draggable(false).title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            moveCameraSmoothly(map, latLng, timeInMills);
            return marker;
        }

        public static Marker addMarker(final GoogleMap map, final LatLng latLng, final int timeInMills) {
            Marker marker = map.addMarker(
                    new MarkerOptions().position(latLng).draggable(false)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            moveCameraSmoothly(map, latLng, timeInMills);
            return marker;
        }

        public static boolean isBetter(Location newLocation, Location current) {
            if (current == null)
                return true;
            if (newLocation == null)
                return false;
            return (newLocation.distanceTo(current) >= MIN_DISTANCE ||
                    (newLocation.getTime() - current.getTime() > MIN_TIME));
        }

        public static LatLng populateLocalStationParams(Context context) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
            return new LatLng(0, 0);
        }

        private static String getUrl(LatLng origin, LatLng dest) {
            String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
            String strDest = "destination=" + dest.latitude + "," + dest.longitude;
            String parameters = strOrigin + "&" + strDest;
            return urlGoogleAPI + parameters;
        }

        public static void direction(GoogleMap mMap, LatLng origin, LatLng dest, RouteParserTask.OnDirectionCallBack callback) {
            String url = getUrl(origin, dest);
            RouteParserTask parserTask = new RouteParserTask(mMap, callback);
            parserTask.execute(url, true);
        }

        public static void direction(LatLng origin, LatLng dest, RouteParserTask.OnDirectionCallBack callback) {
            String url = getUrl(origin, dest);
            RouteParserTask parserTask = new RouteParserTask(callback);
            parserTask.execute(url, false);
        }

        public static void setLocationButtonPosition(SupportMapFragment mapFragment, @Position int position) {

            if (mapFragment.getView() != null &&
                    mapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                View locationButton = ((View) mapFragment.getView()
                        .findViewById(Integer.parseInt("1"))
                        .getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom
                if (position == Position.BOTTOM_RIGHT) {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 0, 30, 30);
                }
            }
        }

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({Position.BOTTOM_RIGHT})
        public @interface Position {
            int BOTTOM_RIGHT = 2;
        }

    }

    public static LatLng getLatLng(@NonNull Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
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

    public interface OnGetLastKnownLocation {
        void onReceive(LatLng latlng);
    }

}