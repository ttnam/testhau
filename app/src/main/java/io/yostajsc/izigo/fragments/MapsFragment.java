package io.yostajsc.izigo.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.glide.CropCircleTransformation;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.dialogs.DialogActiveMembers;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.usecase.backend.core.APIManager;
import io.yostajsc.usecase.firebase.FirebaseManager;
import io.yostajsc.usecase.maps.Track;
import io.yostajsc.usecase.realm.user.FriendRealm;
import io.yostajsc.usecase.realm.user.FriendsRealm;
import io.yostajsc.utils.PrefsUtil;

public class MapsFragment extends CoreFragment implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.text_view_distance)
    TextView textDistance;

    @BindView(R.id.layout)
    FrameLayout layout;

    @BindView(R.id.text_view_name)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    private GoogleMap mMap = null;
    private String mTripId, mFbId, mFocus;
    private FriendsRealm mCurrentActiveMembers = null;
    private HashMap<String, Track> mTracks = new HashMap<>();
    private DialogActiveMembers mDialogActiveMembers = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mTripId = PrefsUtil.inject(mContext).getString(Trip.TRIP_ID);

        if (NetworkUtils.isNetworkConnected(mContext)) {
            APIManager.connect().getMembers(mTripId, new CallBackWith<FriendsRealm>() {
                @Override
                public void run(FriendsRealm friends) {
                    for (FriendRealm friendRealm : friends) {
                        mTracks.put(friendRealm.getFbId(), new Track());
                    }
                    mCurrentActiveMembers = friends;
                    mFocus = mFbId = mCurrentActiveMembers.get(0).getFbId();
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    AppConfig.showToast(mContext, error);
                }
            }, null);
        }
        if (ValidateUtils.canUse(mTripId, mFbId)) {
            AppConfig.getInstance().startLocationServer(mTripId, mFbId);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppConfig.getInstance().stopLocationService();
        FirebaseManager.inject().unregisterListenerOnTrack();
    }


    private void processingUpdateUiOnFocusMember(FriendRealm friendRealm) {

        if (friendRealm == null)
            return;
        layout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(friendRealm.getAvatar())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        this.textName.setText(friendRealm.getName());
        this.textDistance.setText("5 km");
        mFocus = friendRealm.getFbId();
    }

    @OnClick(R.id.image_close)
    public void closeInfoPanel() {
        layout.setVisibility(View.GONE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        mFocus = marker.getTitle();
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMaxZoomPreference(20f);
        this.mMap.setMinZoomPreference(2f);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Register fire base data change listener
        registerDataChangeListener();
    }

    private void registerDataChangeListener() {
        FirebaseManager.inject().registerListenerOnTrack(mTripId, new CallBackWith<DataSnapshot>() {
            @Override
            public void run(DataSnapshot dataSnapshot) {
                processingOnLocationChange(dataSnapshot);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                AppConfig.showToast(mContext, error);
            }
        });
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

    private void onApplyViews() {
        SupportMapFragment m = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        m.getMapAsync(this);

        mDialogActiveMembers = new DialogActiveMembers(mContext, new CallBackWith<FriendRealm>() {
            @Override
            public void run(FriendRealm friendRealm) {
                processingUpdateUiOnFocusMember(friendRealm);
            }
        });
    }

    @OnClick(R.id.button)
    public void showActiveMembersList() {
        mDialogActiveMembers.show();
        mDialogActiveMembers.setData(mCurrentActiveMembers);
    }
}
