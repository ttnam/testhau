package io.yostajsc.izigo.usecases.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import io.yostajsc.izigo.usecases.DataParser;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.cache.IgCache;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.sdk.model.trip.IgImage;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.utils.LocationUtils;
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
    private List<IgFriend> mMembers = null;
    private HashMap<String, Person> mTracks = null;
    private ClusterManager<Person> mClusterManager;
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
        this.mMembers = new ArrayList<>();

        mTripId = PrefsUtils.inject(this).getString(IgTrip.TRIP_ID);
        AppConfig.getInstance().startLocationServer(mTripId);

        if (NetworkUtils.isNetworkConnected(this)) {

            IzigoSdk.TripExecutor.getMembers(mTripId, new IGCallback<List<IgFriend>, String>() {
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
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.mDialogActiveMembers = new DialogActiveMembers(this, new CallBackWith<IgFriend>() {
            @Override
            public void run(IgFriend igFriend) {
                mFocus = igFriend.getFbId();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mTracks.get(mFocus).getPosition()));
            }
        });
    }

    private void onReceiveMembers(List<IgFriend> igFriends) {
        if (igFriends.size() > 0) {
            mMembers = igFriends;
            mFocus = mMembers.get(0).getFbId();
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

    @OnClick(R.id.button)
    public void showActiveMembersList() {
        mDialogActiveMembers.show();
        mDialogActiveMembers.setData(mMembers);
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

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(mTracks.get(mFocus).getPosition()));

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
        this.mMap.setOnMyLocationChangeListener(this);
        this.mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.getUiSettings().setCompassEnabled(true);
        this.mMap.getUiSettings().setMapToolbarEnabled(false);
        this.mMap.getUiSettings().setRotateGesturesEnabled(true);
        this.mMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);

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
        direction(
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
        if (!LocationUtils.Gps.connect().isEnable())
            LocationUtils.Gps.request(this).askGPS();
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

    private class ParserTask extends AsyncTask<Object, Void, String> {

        private boolean draw;
        private CallBackWith<Info> callback;

        @Override
        protected String doInBackground(Object... obj) {

            draw = (boolean) obj[1];
            callback = (CallBackWith<Info>) obj[2];

            String data = "";
            try {
                data = downloadUrl((String) obj[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject jObject;
            List<Route> routes;

            try {
                jObject = new JSONObject(result);

                DataParser parser = new DataParser();
                routes = parser.parse(jObject, draw);

                callback.run(routes.get(0).info);

                if (draw)
                    draw(routes);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void draw(List<Route> routes) {

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = routes.get(i).route;

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.RED);
            }

            if (lineOptions != null) {
                mPolyline = mMap.addPolyline(lineOptions);
             //   mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        String parameters = strOrigin + "&" + strDest;
        return urlGoogleAPI + parameters;
    }

    private void direction(LatLng origin, LatLng dest, boolean draw, CallBackWith<Info> callback) {
        String url = getUrl(origin, dest);
        ParserTask parserTask = new ParserTask();
        parserTask.execute(url, draw, callback);
    }


    private final String urlGoogleAPI = "https://maps.googleapis.com/maps/api/directions/json?language=vi&";
}
