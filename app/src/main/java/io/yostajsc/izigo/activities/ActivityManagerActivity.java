package io.yostajsc.izigo.activities;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.core.ActivityCoreBehavior;
import io.yostajsc.view.OwnToolBar;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ActivityManagerActivity extends ActivityCoreBehavior implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapLongClickListener {

    @BindView(R.id.own_toolbar)
    OwnToolBar toolBar;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    public void onApplyViews() {
        toolBar.setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Setup map view
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onInternetConnected() {

    }

    @Override
    public void onInternetDisConnected() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityManagerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void requestLocation() {
        mMap.setMyLocationEnabled(true); // Enable
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMaxZoomPreference(15.0f);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setTiltGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        ActivityManagerActivityPermissionsDispatcher.requestLocationWithCheck(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}
