package io.yostajsc.izigo.activities.trip;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.glide.CropCircleTransformation;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogActiveMembers;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.usecase.firebase.FirebaseManager;
import io.yostajsc.usecase.maps.Track;
import io.yostajsc.usecase.realm.user.FriendRealm;
import io.yostajsc.usecase.realm.user.FriendsRealm;
import io.yostajsc.usecase.backend.core.APIManager;
import io.yostajsc.utils.LocationUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapsActivity extends OwnCoreActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    @BindView(R.id.text_view_name)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    @BindView(R.id.text_view_distance)
    TextView textDistance;

    @BindView(R.id.layout)
    FrameLayout layout;

    private GoogleMap mMap;
    private String tripId = "-KXtf3MHoje8g3myeJnA";
    private String fbId = "1708620562761348";
    private String focus = fbId;
    private boolean isFirst = true;
    private FriendsRealm mCurrentActiveMembers = null;
    private DialogActiveMembers mDialogActiveMembers = null;
    private HashMap<String, Track> mTracks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppConfig.getInstance().startLocationServer(tripId, fbId);

        mDialogActiveMembers = new DialogActiveMembers(this, new CallBackWith<FriendRealm>() {
            @Override
            public void run(FriendRealm friendRealm) {
                processingUpdateUiOnFocusMember(friendRealm);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        AppConfig.getInstance().stopLocationService();
        FirebaseManager.inject().unregisterListenerOnTrack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MessageType.USER_GPS) {
            if (resultCode != RESULT_OK) {
                MapsActivityPermissionsDispatcher.askGPSWithCheck(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onApplyViews() {

        // Maps initialization
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onApplyData() {

        if (NetworkUtils.isNetworkConnected(this)) {
            processingOnGetActiveMembers();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Maps config
        this.mMap = googleMap;
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mMap.setOnMarkerClickListener(this);

        // Ask GPS
        MapsActivityPermissionsDispatcher.askGPSWithCheck(this);

        // Register fire base data change listener
        registerDataChangeListener();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        focus = marker.getTitle();
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void askGPS() {
        if (!LocationUtils.Gps.connect().isEnable())
            LocationUtils.Gps.request(this).askGPS();
        mMap.setMyLocationEnabled(true); // Enable
    }

    @Override
    protected void onGpsOff() {
        super.onGpsOff();
        MapsActivityPermissionsDispatcher.askGPSWithCheck(this);
    }

    @OnClick(R.id.button)
    public void showActiveMembersList() {
        mDialogActiveMembers.show();
        mDialogActiveMembers.setData(mCurrentActiveMembers);
    }

    private void registerDataChangeListener() {
        FirebaseManager.inject().registerListenerOnTrack(tripId, new CallBackWith<DataSnapshot>() {
            @Override
            public void run(DataSnapshot dataSnapshot) {
                processingOnLocationChange(dataSnapshot);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(MapsActivity.this, error);
            }
        });
    }

    private void processingOnGetActiveMembers() {
        APIManager.connect().getMembers(tripId, new CallBackWith<FriendsRealm>() {
            @Override
            public void run(FriendsRealm friends) {
                processingOnReceiveFriend(friends);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(MapsActivity.this, error);
            }
        }, mOnExpiredCallBack);
    }

    private void processingOnReceiveFriend(FriendsRealm friendRealms) {
        for (FriendRealm friendRealm : friendRealms) {
            mTracks.put(friendRealm.getFbId(), new Track());
        }
        mCurrentActiveMembers = friendRealms;
    }

    private void processingOnLocationChange(DataSnapshot dataSnapshot) {
        try {
            for (String fbId : mTracks.keySet()) {
                if (dataSnapshot.hasChild(fbId)) {
                    Iterator<DataSnapshot> geoIterator = dataSnapshot
                            .child(fbId).child("geo").getChildren().iterator();
                    DataSnapshot geoData = null;
                    while (geoIterator.hasNext())
                        geoData = geoIterator.next();

                    String[] dataChild = ((String) geoData.getValue()).split(", ");

                    mTracks.get(fbId)
                            .setLatLng(new LatLng(
                                    Double.parseDouble(dataChild[0]),           // Lat
                                    Double.parseDouble(dataChild[1])))          // Lng
                            .setUpdateAt(Long.parseLong(geoData.getKey()))      // Time update
                            .setVisible(dataChild[3].equalsIgnoreCase("1"));    // Is visible
                }
            }
            // Update camera position
            /*if (tempFbId.equals(focus))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mTracks.get(fbId).getLatLng()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processingUpdateUiOnFocusMember(FriendRealm friendRealm) {

        if (friendRealm == null)
            return;
        layout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(friendRealm.getAvatar())
                .bitmapTransform(new CropCircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        this.textName.setText(friendRealm.getName());
        this.textDistance.setText("5 km");
    }

}
