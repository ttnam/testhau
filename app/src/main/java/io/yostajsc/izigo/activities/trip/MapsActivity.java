package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.yostajsc.izigo.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker mOwnMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        onApplyViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void requestLocation() {
        mMap.setMyLocationEnabled(true); // Enable
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationChangeListener(this);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        MapsActivityPermissionsDispatcher.requestLocationWithCheck(this);
    }

    private void onApplyViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMyLocationChange(Location location) {
        moveOwnMarker(location, false);
    }

    private void moveOwnMarker(final Location location, final boolean hideMarker) {
        if (location != null) {
            if (mOwnMarker != null)
                mOwnMarker.remove();
            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mOwnMarker = mMap.addMarker(new MarkerOptions()
                    .draggable(false)
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_marker))
            );

            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection projection = mMap.getProjection();
            Point startPoint = projection.toScreenLocation(mOwnMarker.getPosition());
            final LatLng startLatLng = projection.fromScreenLocation(startPoint);
            final long duration = 500;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * latLng.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * latLng.latitude + (1 - t)
                            * startLatLng.latitude;

                    mOwnMarker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            mOwnMarker.setVisible(false);
                        } else {
                            mOwnMarker.setVisible(true);
                        }
                    }
                }
            });
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }
}
