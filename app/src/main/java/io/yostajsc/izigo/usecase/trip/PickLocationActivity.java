package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.sdk.api.model.trip.IgPlace;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.sdk.consts.MessageType;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PickLocationActivity extends OwnCoreActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private final static String TAG = PickLocationActivity.class.getSimpleName();

    private GoogleMap mMap;
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
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        this.mapFragment.getMapAsync(this);

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

                mMap.clear();
                igLocation.setName(place.getName().toString());

                LatLng latLng = place.getLatLng();
                igLocation.setLatLng(latLng.latitude, latLng.longitude);

                MapUtils.Map.addMarker(mMap, latLng, 500);
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
                if (resultCode != Activity.RESULT_OK)
                    PickLocationActivityPermissionsDispatcher.requestLocationWithCheck(PickLocationActivity.this);
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
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    protected void onGpsOff() {
        super.onGpsOff();
        PickLocationActivityPermissionsDispatcher.requestLocationWithCheck(PickLocationActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMaxZoomPreference(15.0f);
        this.mMap.setMinZoomPreference(5.0f);
        this.mMap.getUiSettings().setMapToolbarEnabled(false);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        PickLocationActivityPermissionsDispatcher.requestLocationWithCheck(this);
    }

    @OnClick({R.id.button, R.id.image_view})
    public void pick() {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.KEY_PICK_LOCATION, igLocation);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
