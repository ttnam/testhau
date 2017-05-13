package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.sdk.model.trip.IgPlace;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PickLocationActivity extends OwnCoreActivity implements GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationChangeListener {

    private final static String TAG = PickLocationActivity.class.getSimpleName();

    private GoogleMap mMap;
    private String locationName = "";
    private LatLng currLatLng = new LatLng(0, 0);
    private Location currLocation = null;
    private IgPlace igLocation = new IgPlace();
    private MyPlaceSelector myPlaceSelector = null;
    private SupportMapFragment mapFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    public void onApplyViews() {

        // Setup map view
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        // Place auto complete fragment
        this.myPlaceSelector = (MyPlaceSelector) getFragmentManager().findFragmentById(R.id.map_place);
        this.myPlaceSelector.setInputBackground(getResources().getColor(android.R.color.white));
        this.myPlaceSelector.setVisibilityOfSearchIcon(View.GONE);
        this.myPlaceSelector.setOnClearButtonClickListener(new MyPlaceSelector.CallBack() {
            @Override
            public void run() {

            }
        });
        this.myPlaceSelector.setOnPlaceSelectedListener(new MyPlaceSelector.OnPlaceSelecteListener() {
            @Override
            public void select(Place place) {
                locationName = place.getName().toString();
                LatLng latLng = place.getLatLng();
                currLatLng = latLng;
                MapUtils.Map.addShowCustomMarker(mMap, latLng);
                myPlaceSelector.setText(place.getName().toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PickLocationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MessageType.USER_GPS:
                if (resultCode != Activity.RESULT_OK) {
                    currLatLng = MapUtils.Map.populateLocalStationParams(this);
                    MapUtils.Map.addShowCustomMarker(mMap, currLatLng);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void requestLocation() {
        if (!MapUtils.Gps.connect().isEnable())
            MapUtils.Gps.request(this).askGPS();
        else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            MapUtils.Map.setLocationButtonPosition(this.mapFragment,
                    MapUtils.Map.Position.BOTTOM_RIGHT);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.currLatLng = latLng;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (MapUtils.Map.isBetter(location, currLocation)) {
            currLocation = location;
            currLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
            MapUtils.Map.addShowCustomMarker(mMap, currLatLng);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        PickLocationActivityPermissionsDispatcher.requestLocationWithCheck(this);

        this.mMap.setMaxZoomPreference(15.0f);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setTiltGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    @OnClick({R.id.button, R.id.image_view})
    public void pick() {
        Intent intent = new Intent();
        igLocation.setName(locationName);
        igLocation.setLat(currLatLng.latitude);
        igLocation.setLng(currLatLng.longitude);
        igLocation.setName(locationName);
        intent.putExtra(AppConfig.KEY_PICK_LOCATION, igLocation);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
