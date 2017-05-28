package io.yostajsc.izigo.usecase.map;

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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.usecase.map.utils.RouteParserTask;
import io.yostajsc.izigo.usecase.webview.WebViewActivity;
import io.yostajsc.sdk.api.model.IgSuggestion;
import io.yostajsc.sdk.consts.CallBack;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.utils.LogUtils;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.utils.NetworkUtils;
import io.yostajsc.izigo.usecase.service.firebase.FirebaseExecutor;
import io.yostajsc.izigo.usecase.service.firebase.FirebaseManager;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.trip.dialog.DialogActiveMembers;
import io.yostajsc.izigo.usecase.view.OwnToolBar;
import io.yostajsc.izigo.usecase.map.utils.Info;
import io.yostajsc.izigo.usecase.map.model.Person;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.izigo.usecase.notification.DialogNotification;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.cache.IgCache;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.IgNotification;
import io.yostajsc.sdk.api.model.trip.IgImage;
import io.yostajsc.sdk.api.model.user.IgFriend;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapsActivity extends OwnCoreActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationChangeListener,
        ClusterManager.OnClusterClickListener<Person>,
        GoogleMap.OnMarkerClickListener, DialogActiveMembers.OnItemSelectListener,
        DialogMapSetting.OnOnlineListener, GoogleMap.OnMapClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap = null;

    private Polyline mPolyline = null;
    private String mFocus = null, fbTemp = null;
    private boolean mIsFirst = true, mIsDraw = false;

    private LatLng mOwnLatLng;
    private boolean mIsSuggestion = false;
    private SupportMapFragment mapFragment = null;
    private DialogMapSetting mDialogMapSetting = null;
    private DialogNotification mDialogNotification = null;
    private ClusterManager<Person> mClusterManager = null;
    private DialogActiveMembers mDialogActiveMembers = null;
    private HashMap<String, Person> mTracks = new HashMap<>();
    private HashMap<Marker, IgSuggestion> mSuggestions = new HashMap<>();
    private List<IgNotification> notifications = new ArrayList<>();

    @BindView(R.id.own_toolbar)
    OwnToolBar ownToolbar;

    @BindView(R.id.button_direction)
    FloatingActionButton btnDirection;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.image_suggest_cover)
    AppCompatImageView imageSuggestCover;

    @BindView(R.id.text_suggest_description)
    TextView textSuggestDescription;

    @BindView(R.id.text_suggest_name)
    TextView textSuggestName;

    @BindView(R.id.text_suggest_type)
    TextView textSuggestType;

    @BindView(R.id.text_suggest_distance)
    TextView textSuggestDistance;

    @BindView(R.id.layout_suggestions)
    CardView layoutSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        MapsActivityView.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);
        this.mDialogMapSetting = new DialogMapSetting(this, this);
        this.mDialogActiveMembers = new DialogActiveMembers(this, this);
        this.mDialogNotification = new DialogNotification(MapsActivity.this);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        AppConfig.getInstance().startLocationServer();

        if (NetworkUtils.isNetworkConnected(this)) {
            String tripId = AppConfig.getInstance().getCurrentTripId();
            if (!TextUtils.isEmpty(tripId)) {
                IzigoSdk.TripExecutor.getMembers(tripId,
                        new IgCallback<List<IgFriend>, String>() {
                            @Override
                            public void onSuccessful(List<IgFriend> igFriends) {
                                onReceiveMembers(igFriends);
                            }

                            @Override
                            public void onFail(String error) {
                                LogUtils.log(TAG, error);
                            }

                            @Override
                            public void onExpired() {
                                expired();
                            }
                        });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapsActivityView.unbind();
        AppConfig.getInstance().stopLocationService();
        FirebaseManager.inject().unregisterListenerOnTrack();
    }

    @Override
    public void selected(String fbId) {
        mFocus = fbId;
        MapUtils.Map.moveCameraSmoothly(mMap, mTracks.get(mFocus).getPosition(), 500);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;

        // Ask permission to enable location
        MapsActivityPermissionsDispatcher.enableMyLocationWithCheck(this);

        this.mMap.setTrafficEnabled(true);
        this.mMap.setMinZoomPreference(5f);
        this.mMap.setMaxZoomPreference(14f);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setMapToolbarEnabled(false);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);

        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // Cluster renderer
        this.mClusterManager = new ClusterManager<>(this, this.mMap);
        this.mClusterManager.setRenderer(new PersonRenderer(this));
        this.mClusterManager.setOnClusterClickListener(this);
        this.mMap.setOnCameraIdleListener(this.mClusterManager);

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
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds(latLngBounds.southwest, latLngBounds.northeast), 180));

        return true;
    }

    @OnClick(R.id.button_direction)
    public void showDirection() {

        mIsDraw = true;

        String ownFbId = IzigoSdk.UserExecutor.getOwnFbId();

        if (mPolyline != null)
            mPolyline.remove();

        MapUtils.Map.direction(mMap,
                mTracks.get(ownFbId).getPosition(), // from
                mTracks.get(mFocus).getPosition(), // to
                true, new RouteParserTask.OnDirectionCallBack() {
                    @Override
                    public void onSuccess(Info info, Polyline polyline) {
                        mPolyline = polyline;
                    }
                });
    }

    private void showDirection(String fbId) {
        String ownFbId = IzigoSdk.UserExecutor.getOwnFbId();

        if (!mFocus.equalsIgnoreCase(fbId))
            return;

        if (mPolyline != null)
            mPolyline.remove();

        MapUtils.Map.direction(mMap,
                mTracks.get(ownFbId).getPosition(), // from
                mTracks.get(mFocus).getPosition(), // to
                true, new RouteParserTask.OnDirectionCallBack() {
                    @Override
                    public void onSuccess(Info info, Polyline polyline) {
                        mPolyline = polyline;
                    }
                });
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void enableMyLocation() {
        if (!MapUtils.Gps.connect().isEnable())
            MapUtils.Gps.request(this).askGPS();
        else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            MapUtils.Map.setLocationButtonPosition(this.mapFragment, MapUtils.Map.Position.BOTTOM_RIGHT);
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (mIsSuggestion) {
            IgSuggestion suggestion = mSuggestions.get(marker);
            if (suggestion != null) {
                MapsActivityView.setSuggestion(
                        suggestion.getName(),
                        suggestion.getCover(),
                        suggestion.getDescription(),
                        suggestion.getType(),
                        mOwnLatLng,
                        new LatLng(suggestion.getLat(), suggestion.getLng())
                );
                layoutSuggestion.setVisibility(View.VISIBLE);
            }
        } else {
            MapUtils.Map.moveCameraSmoothly(mMap, marker.getPosition(), 500);
            mFocus = marker.getTitle();
            if (!TextUtils.isEmpty(mFocus)) {
                if (mFocus.equalsIgnoreCase(IzigoSdk.UserExecutor.getOwnFbId())) {
                    btnDirection.setVisibility(View.GONE);
                    mIsDraw = false;
                    if (mPolyline != null)
                        mPolyline.remove();
                } else {
                    btnDirection.setVisibility(View.VISIBLE);
                }
            }
        }
        return true;
    }


    @OnClick(R.id.layout_notification)
    public void loadNotifications() {
        closeMenu();
        if (!mDialogNotification.isShowing())
            mDialogNotification.show();

        IzigoSdk.UserExecutor.getNotifications("3;4", new IgCallback<List<IgNotification>, String>() {
            @Override
            public void onSuccessful(List<IgNotification> data) {
                if (data.size() > 0) {
                    notifications.clear();
                    for (IgNotification notification : data) {
                        notifications.add(notification);
                    }
                    mDialogNotification.setData(notifications);
                }
            }

            @Override
            public void onFail(String error) {
                LogUtils.log(TAG, error);
                mDialogNotification.dismiss();
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    @OnClick(R.id.layout_members)
    public void showActiveMembersList() {

        closeMenu();

        LatLng latLngOwn = mTracks.get(IzigoSdk.UserExecutor.getOwnFbId()).getPosition();

        mDialogActiveMembers.show();

        for (final String fbId : mTracks.keySet()) {

            fbTemp = fbId;
            LatLng latLng = mTracks.get(fbTemp).getPosition();
            if (latLng == null)
                return;
            MapUtils.Map.direction(mMap, latLngOwn, latLng, false, new RouteParserTask.OnDirectionCallBack() {
                @Override
                public void onSuccess(Info info, Polyline polyline) {
                    mTracks.get(fbTemp).setDistance(info.strDistance);
                    mTracks.get(fbTemp).setTime(info.strDuration);
                    mDialogActiveMembers.setData(mTracks.values().toArray(new Person[mTracks.values().size()]));
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mIsSuggestion = false;
        for (Map.Entry<Marker, IgSuggestion> entry : mSuggestions.entrySet()) {
            entry.getKey().remove();
        }
        layoutSuggestion.setVisibility(View.GONE);
    }

    @OnClick(R.id.layout_suggestion)
    public void showSuggestion() {
        closeMenu();
        mIsSuggestion = true;
        if (MapUtils.Gps.connect().isEnable()) {
            MapUtils.Gps.connect().getLastKnownLocation(getApplicationContext(), new MapUtils.OnGetLastKnownLocation() {
                @Override
                public void onReceive(LatLng latlng) {
                    if (latlng != null) {
                        mOwnLatLng = latlng;
                        IzigoSdk.TripExecutor.getSuggestion(latlng.latitude, latlng.longitude,
                                new IgCallback<List<IgSuggestion>, String>() {
                                    @Override
                                    public void onSuccessful(List<IgSuggestion> igSuggestions) {
                                        showSuggestion(igSuggestions);
                                    }

                                    @Override
                                    public void onFail(String error) {
                                        LogUtils.log(TAG, error);
                                    }

                                    @Override
                                    public void onExpired() {
                                        expired();
                                    }
                                });
                    }
                }
            });
        } else {
            MapUtils.Gps.connect().askGPS();
        }
    }

    @OnClick(R.id.layout_setting)
    public void setting() {
        closeMenu();
        mDialogMapSetting.show();
    }

    @Override
    public void run(boolean isOnline) {
        AppConfig.getInstance().setAvailable(isOnline);
        FirebaseExecutor.TripExecutor.online(
                AppConfig.getInstance().getCurrentTripId(),
                IzigoSdk.UserExecutor.getOwnFbId(),
                isOnline);
    }

    @OnClick(R.id.layout_suggestions)
    public void openWebView() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("WEB_LINK", "");
        startActivity(intent);
    }

    private void onChildAdded(String fbId, double lat, double lng, String time, boolean isOnline) {

        // Update
        this.mTracks.get(fbId).setVisible(isOnline);                // Is online
        this.mTracks.get(fbId).setLatLng(new LatLng(lat, lng));     // Location
        this.mTracks.get(fbId).setUpdateAt(Long.parseLong(time));   // Time

        // Show maps
        Person person = this.mTracks.get(fbId);
        if (person.getPosition() != null) {

            // Download avatar
            IgCache.BitmapsCache.askForMemory().cache(new CallBack() {
                @Override
                public void run() {

                }
            }, new IgImage(person.getId(), person.getAvatar()));

            // Add new cluster Item
            mClusterManager.addItem(person);
            mClusterManager.cluster();
        }
    }

    private void onChildChanged(String fbId, double lat, double lng, String time, boolean isOnline) {
        // Update the new one
        this.mTracks.get(fbId).setVisible(isOnline);                // Is online
        this.mTracks.get(fbId).setLatLng(new LatLng(lat, lng));     // Location
        this.mTracks.get(fbId).setUpdateAt(Long.parseLong(time));   // Time
        reloadMarker(this.mTracks.get(fbId));
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
            View singleProfile = getLayoutInflater().inflate(R.layout.layout_single_profile, null);
            mSingleImageView = (AppCompatImageView) singleProfile.findViewById(R.id.image_view);
            mSingleGenerator.setContentView(singleProfile);

            // Multi profile
            View multiProfile = getLayoutInflater().inflate(R.layout.layout_multiple_profile, null);
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

    private void reloadMarker(Person person) {
        Collection<Marker> markers = mClusterManager.getMarkerCollection().getMarkers();
        String strId = person.getId();
        for (Marker marker : markers) {
            if (strId.equalsIgnoreCase(marker.getTitle())) {
                marker.setPosition(person.getPosition());
                if (mIsDraw)
                    showDirection(marker.getTitle());
                break;
            }
        }
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
            FirebaseManager.inject().subscribeLastGps(AppConfig.getInstance().getCurrentTripId(),
                    new FirebaseManager.OnSuccessListener() {
                        @Override
                        public void success(String fbId, double lat, double lng, String time, boolean isOnline) {
                            onChildAdded(fbId, lat, lng, time, isOnline);
                        }
                    }, new FirebaseManager.OnSuccessListener() {
                        @Override
                        public void success(String fbId, double lat, double lng, String time, boolean isOnline) {
                            onChildChanged(fbId, lat, lng, time, isOnline);
                        }
                    }, new FirebaseManager.OnFailureListener() {
                        @Override
                        public void error(String error) {
                            ToastUtils.showToast(MapsActivity.this, error);
                        }
                    });
        }
    }

    public void openMenu() {
        drawerLayout.openDrawer(Gravity.END);
    }

    private void closeMenu() {
        drawerLayout.closeDrawers();
    }

    private void showSuggestion(List<IgSuggestion> igSuggestions) {
        if (igSuggestions != null && igSuggestions.size() > 0) {
            mSuggestions.clear();
            for (IgSuggestion igSuggestion : igSuggestions) {

                Marker marker = MapUtils.Map.addMarker(mMap,
                        new LatLng(igSuggestion.getLat(), igSuggestion.getLng()), igSuggestion.getId(), 500);
                mSuggestions.put(marker, igSuggestion);
            }
        } else {
            ToastUtils.showToast(MapsActivity.this, "No suggestion.");
        }
    }

}
