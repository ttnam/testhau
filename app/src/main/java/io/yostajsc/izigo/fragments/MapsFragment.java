package io.yostajsc.izigo.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.maps.android.clustering.Cluster;
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
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.cache.IgCache;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.AppConfig;
import io.yostajsc.izigo.dialogs.DialogActiveMembers;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.usecase.firebase.FirebaseManager;

public class MapsFragment extends CoreFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener
        /*GoogleMap.OnMarkerClickListener, ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>*/ {

    private static final String TAG = MapsFragment.class.getSimpleName();

    @BindView(R.id.text_view_distance)
    TextView textDistance;

    @BindView(R.id.layout)
    FrameLayout layout;

    @BindView(R.id.text_view_name)
    TextView textName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;


    private boolean mIsFirst = false;

    private LatLngBounds mFirstBound = null;
    private Person mCurrentPerson = null;

    private DialogActiveMembers mDialogActiveMembers = null;


    private GoogleMap mMap = null;
    private List<IgFriend> mMembers = null;
    private String mTripId = null, mFocus = null;
    private HashMap<String, Person> mTracks = null;

    private ClusterManager<Person> mClusterManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, rootView);

        onApplyViews();

        this.mTracks = new HashMap<>();
        this.mMembers = new ArrayList<>();

        return rootView;
    }

    private void onApplyViews() {

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        this.mDialogActiveMembers = new DialogActiveMembers(mContext, new CallBackWith<IgFriend>() {
            @Override
            public void run(IgFriend igFriend) {
                mFocus = igFriend.getFbId();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mTracks.get(mFocus).getPosition()));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        mTripId = PrefsUtils.inject(mContext).getString(IgTrip.TRIP_ID);

        // Register fire base data change listener
        registerDataChangeListener();

        if (NetworkUtils.isNetworkConnected(mContext)) {

            IzigoSdk.TripExecutor.getMembers(mTripId, new IGCallback<List<IgFriend>, String>() {
                @Override
                public void onSuccessful(List<IgFriend> igFriends) {
                    mMembers = igFriends;
                    mFocus = mMembers.get(0).getFbId();
                    for (IgFriend friend : igFriends) {
                        mTracks.put(friend.getFbId(), new Person(
                                friend.getFbId(),
                                friend.getName(),
                                friend.getAvatar()
                        ));
                    }
                }

                @Override
                public void onFail(String error) {
                    AppConfig.showToast(mContext, error);
                }

                @Override
                public void onExpired() {

                }
            });
        }
    }

    @OnClick(R.id.button)
    public void showActiveMembersList() {
        mDialogActiveMembers.show();
        mDialogActiveMembers.setData(mMembers);
    }

    @Override
    public void onMyLocationChange(Location location) {
        /*if (mFocus == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMaxZoomPreference(20f);
        this.mMap.setMinZoomPreference(2f);
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Cluster renderer
        this.mClusterManager = new ClusterManager<>(mContext, mMap);
        this.mClusterManager.setRenderer(new PersonRenderer());
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
                AppConfig.showToast(mContext, error);
            }
        });
    }

    private void onLocationsChange(DataSnapshot dataSnapshot) {
        for (String fbId : mTracks.keySet()) {
            if (dataSnapshot.hasChild(fbId)) {

                // Get list location
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

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(mTracks.get(mFocus).getPosition()));

        makeCluster();
    }

    private void clearAll() {
        if (this.mClusterManager != null) {
            this.mClusterManager.clearItems();
        }
    }

    private void makeCluster() {

        // Not initialize yet
        if (mClusterManager == null)
            return;

        clearAll();

        // Call caching
        List<IgImage> igImages = new ArrayList<>();

        // Convert to cluster
        for (Map.Entry<String, Person> entry : mTracks.entrySet()) {
            mClusterManager.addItem(entry.getValue());
            igImages.add(new IgImage(entry.getKey(), entry.getValue().getAvatar()));
        }
        // Bitmaps caching
        IgCache.BitmapsCache.askForMemory().cache(new CallBack() {
            @Override
            public void run() {
                mClusterManager.cluster();
            }
        }, igImages.toArray(new IgImage[mTracks.size()]));
    }
/*


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppConfig.getInstance().stopLocationService();
        FirebaseManager.inject().unregisterListenerOnTrack();
    }

    private void processingUpdateUiOnFocusMember(IgFriend igFriend) {

        if (igFriend == null)
            return;
        layout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(igFriend.getAvatar())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        this.textName.setText(igFriend.getName());
        this.textDistance.setText("5 km");
        mFocus = igFriend.getFbId();
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
    public boolean onClusterClick(Cluster<Person> cluster) {
        try {
            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (ClusterItem item : cluster.getItems()) {
                builder.include(item.getPosition());
            }
            // Get the LatLngBounds
            final LatLngBounds latLngBounds = builder.build();
            if (mIsFirst)
                mFirstBound = latLngBounds;
            // Animate camera to the bounds

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
                    latLngBounds.southwest,
                    latLngBounds.northeast), 150));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onClusterItemClick(Person person) {
        try {
            this.mCurrentPerson = person;
            // this.layoutInfo.setVisibility(View.VISIBLE);
            */
/*this.textName.setText(person.getName());
            this.textDistance.setText(person.getDistance() + " km");
            Glide.with(mContext)
                    .load(person.getAvatar())
                    .priority(Priority.IMMEDIATE)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageAvatar);*//*

        } catch (Exception e) {
            LogUtils.log(TAG, e.getMessage());
        }
        return false;
    }

*/


    private class PersonRenderer extends DefaultClusterRenderer<Person> {

        private final IconGenerator mSingleGenerator = new IconGenerator(mContext);
        private final IconGenerator mMultiGenerator = new IconGenerator(mContext);
        private final ImageView mSingleImageView, mMultiImageView;
        private int mDimension;

        PersonRenderer() {
            super(mContext, mMap, mClusterManager);

            // Single profile
            View singleProfile = getActivity().getLayoutInflater().inflate(R.layout.layout_marker_profile, null);
            mSingleImageView = (AppCompatImageView) singleProfile.findViewById(R.id.image_view);
            mSingleGenerator.setContentView(singleProfile);

            // Multi profile
            View multiProfile = getActivity().getLayoutInflater().inflate(R.layout.layout_multi_profile, null);
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
                    //onClusterClick(cluster);
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
