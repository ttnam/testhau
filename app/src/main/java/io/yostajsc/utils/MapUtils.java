package io.yostajsc.utils;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by nphau on 4/3/17.
 */

public class MapUtils {

    // Zoom to all markers on Android Map
    public static void zoomToAllMarkersOnMap(GoogleMap googleMap, Marker[] markers) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers)
            builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.animateCamera(cameraUpdate);
    }
}
