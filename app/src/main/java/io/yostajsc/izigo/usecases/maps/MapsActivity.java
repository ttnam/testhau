package io.yostajsc.izigo.usecases.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.AppConfig;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.firebase.FirebaseManager;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogActiveMembers;
import io.yostajsc.izigo.fragments.MultiDrawable;
import io.yostajsc.izigo.fragments.Person;
import io.yostajsc.utils.maps.Info;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.cache.IgCache;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.utils.maps.MapUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapsActivity extends OwnCoreActivity
        implements
        OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        ClusterManager.OnClusterClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person> {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private boolean mIsFirst = true;
    private GoogleMap mMap = null;
    private String mTripId = null, mFocus = null;
    private HashMap<String, Person> mTracks = null;
    private SupportMapFragment mapFragment = null;
    private ClusterManager<Person> mClusterManager = null;
    private DialogActiveMembers mDialogActiveMembers = null;
    private Polyline mPolyline = null;

    @BindView(R.id.button_direction)
    FloatingActionButton btnDirection;

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
        MapsActivityPermissionsDispatcher.askGPSWithCheck(this);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        this.mTracks = new HashMap<>();
        mTripId = PrefsUtils.inject(this).getString(IgTrip.TRIP_ID);
        AppConfig.getInstance().startLocationServer(mTripId);

        if (NetworkUtils.isNetworkConnected(this)) {

            IzigoSdk.TripExecutor.getMembers(mTripId, new IgCallback<List<IgFriend>, String>() {
                @Override
                public void onSuccessful(List<IgFriend> igFriends) {
                    onReceiveMembers(igFriends);
                }

                @Override
                public void onFail(String error) {
                    AppConfig.showToast(MapsActivity.this, error);
                }

                @Override
                public void onExpired() {

                }
            });
        }
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);
        this.mDialogActiveMembers = new DialogActiveMembers(this, new CallBackWith<String>() {
            @Override
            public void run(String fbId) {
                mFocus = fbId;
                MapUtils.Map.moveCameraSmoothly(mMap, mTracks.get(mFocus).getPosition());
            }
        });
    }

    private void onReceiveMembers(List<IgFriend> igFriends) {
        if (igFriends.size() > 0) {
            mFocus = igFriends.get(0).getFbId();
            for (IgFriend friend : igFriends) {
                mTracks.put(friend.getFbId(), new Person(
                        friend.getFbId(),
                        friend.getName(),
                        friend.getAvatar()
                ));
            }
            // Register fire base data change listener
            registerDataChangeListener();
        } else {
            AppConfig.showToast(this, "Chưa có thành viên nào!");
        }
    }

    private String fbTemp;

    @OnClick(R.id.button)
    public void showActiveMembersList() {
        mDialogActiveMembers.show();

        LatLng latLngOwn = mTracks.get(IzigoSdk.UserExecutor.getOwnFbId()).getPosition();

        for (final String fbId : mTracks.keySet()) {

            fbTemp = fbId;
            LatLng latLng = mTracks.get(fbTemp).getPosition();
            MapUtils.Map.direction(mMap, latLngOwn, latLng, false, new CallBackWith<Info>() {
                @Override
                public void run(Info info) {
                    mTracks.get(fbTemp).setDistance(info.strDistance);
                    mTracks.get(fbTemp).setTime(info.strDuration);
                    mDialogActiveMembers.setData(mTracks.values().toArray(new Person[mTracks.values().size()]));
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConfig.getInstance().stopLocationService();
        FirebaseManager.inject().unregisterListenerOnTrack();
    }

    private void registerDataChangeListener() {
        FirebaseManager.inject().registerListenerOnTrack(mTripId, new CallBackWith<DataSnapshot>() {
            @Override
            public void run(DataSnapshot dataSnapshot) {
                onLocationsChange(dataSnapshot);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(MapsActivity.this, error);
            }
        });
    }

    private void onLocationsChange(DataSnapshot dataSnapshot) {

        for (String fbId : mTracks.keySet()) {
            if (dataSnapshot.hasChild(fbId)) {

                // Get list ic_vector_location
                Iterator<DataSnapshot> geoIterator = dataSnapshot
                        .child(fbId)
                        .child("geo")
                        .getChildren().iterator();

                DataSnapshot geoData = null;
                while (geoIterator.hasNext())
                    geoData = geoIterator.next();

                if (geoData == null)
                    return;

                String[] dataChild = ((String) geoData.getValue()).split(", ");
                if (dataChild.length < 3)
                    return;
                mTracks.get(fbId).setLatLng(new LatLng(
                        Double.parseDouble(dataChild[0]),                            // Lat
                        Double.parseDouble(dataChild[1])));                          // Lng
                mTracks.get(fbId).setUpdateAt(Long.parseLong(geoData.getKey()));     // Time update
                mTracks.get(fbId).setVisible(dataChild[2].equalsIgnoreCase("1"));    // Is visible

            }
        }
        if (mTracks.size() > 0)
            makeCluster();
    }

    private void makeCluster() {

        // Not initialize yet
        if (mClusterManager == null)
            return;

        this.mClusterManager.clearItems();

        // Call caching
        List<IgImage> igImages = new ArrayList<>();

        // Convert to cluster
        for (Map.Entry<String, Person> entry : mTracks.entrySet()) {
            Person person = entry.getValue();
            if (person.getPosition() != null) {
                mClusterManager.addItem(person);
                igImages.add(new IgImage(entry.getKey(), entry.getValue().getAvatar()));
            }

        }
        mClusterManager.cluster();
        // Bitmaps caching
        IgCache.BitmapsCache.askForMemory().cache(new CallBack() {
            @Override
            public void run() {

            }
        }, igImages.toArray(new IgImage[mTracks.size()]));
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMaxZoomPreference(15f);
        this.mMap.setMinZoomPreference(4f);
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setMapToolbarEnabled(false);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);

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
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        // Cluster renderer
        this.mClusterManager = new ClusterManager<>(this, mMap);
        this.mClusterManager.setRenderer(new PersonRenderer(this));
        this.mMap.setOnCameraIdleListener(mClusterManager);
        this.mMap.setOnMarkerClickListener(mClusterManager);
        this.mClusterManager.setOnClusterClickListener(this);
        this.mClusterManager.setOnClusterItemClickListener(this);
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds latLngBounds = builder.build();
        // Animate camera to the bounds
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
                latLngBounds.southwest,
                latLngBounds.northeast), 150));

        return true;
    }

    @Override
    public boolean onClusterItemClick(Person person) {

        this.mFocus = person.getId();
        if (mFocus.equalsIgnoreCase(IzigoSdk.UserExecutor.getOwnFbId())) {
            this.btnDirection.setVisibility(View.GONE);
        } else {
            this.btnDirection.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @OnClick(R.id.button_direction)
    public void showDirection() {
        String ownFbId = IzigoSdk.UserExecutor.getOwnFbId();
        MapUtils.Map.direction(mMap,
                mTracks.get(ownFbId).getPosition(), // from
                mTracks.get(mFocus).getPosition(), // to
                true,
                new CallBackWith<Info>() {
                    @Override
                    public void run(Info info) {

                    }
                });
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void askGPS() {
        if (!MapUtils.Gps.connect().isEnable())
            MapUtils.Gps.request(this).askGPS();
    }

    @Override
    protected void onGpsOff() {
        super.onGpsOff();
        MapsActivityPermissionsDispatcher.askGPSWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MessageType.USER_GPS) {
            if (resultCode != RESULT_OK) {
                MapsActivityPermissionsDispatcher.askGPSWithCheck(this);
            }
        }
    }

    private class PersonRenderer extends DefaultClusterRenderer<Person> {

        private final IconGenerator mSingleGenerator;
        private final IconGenerator mMultiGenerator;
        private final ImageView mSingleImageView, mMultiImageView;
        private int mDimension;

        PersonRenderer(Context context) {
            super(context, mMap, mClusterManager);

            mSingleGenerator = new IconGenerator(getApplicationContext());
            mMultiGenerator = new IconGenerator(getApplicationContext());

            // Single profile
            View singleProfile = getLayoutInflater().inflate(R.layout.layout_marker_profile, null);
            mSingleImageView = (AppCompatImageView) singleProfile.findViewById(R.id.image_view);
            mSingleGenerator.setContentView(singleProfile);

            // Multi profile
            View multiProfile = getLayoutInflater().inflate(R.layout.layout_multi_profile, null);
            mMultiGenerator.setContentView(multiProfile);
            mMultiImageView = (AppCompatImageView) multiProfile.findViewById(R.id.image_view);

            mDimension = (int) getResources().getDimension(R.dimen.margin_x7);

        }

        @Override
        protected void onClusterItemRendered(Person person, final Marker marker) {
            super.onClusterItemRendered(person, marker);
            try {
                Bitmap bitmap = IgCache.BitmapsCache.askForMemory().get(person.getId());
                mSingleImageView.setImageBitmap(bitmap);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(mSingleGenerator.makeIcon()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onBeforeClusterItemRendered(final Person person, final MarkerOptions markerOptions) {
            try {
                Bitmap bitmap = IgCache.BitmapsCache.askForMemory().get(person.getId());
                mSingleImageView.setImageBitmap(bitmap);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mSingleGenerator.makeIcon()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            try {
                int width = mDimension;
                int height = mDimension;
                List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));
                for (Person person : cluster.getItems()) {
                    if (profilePhotos.size() == 4)
                        break;
                    Drawable drawable = new BitmapDrawable(getResources(),
                            IgCache.BitmapsCache.askForMemory().get(person.getId()));
                    drawable.setBounds(0, 0, width, height);
                    profilePhotos.add(drawable);
                }
                MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
                multiDrawable.setBounds(0, 0, width, height);

                mMultiImageView.setImageDrawable(multiDrawable);
                Bitmap icon = mMultiGenerator.makeIcon(String.valueOf(cluster.getSize()));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
                if (mIsFirst) {
                    onClusterClick(cluster);
                    mIsFirst = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > 1;
        }
    }

}
