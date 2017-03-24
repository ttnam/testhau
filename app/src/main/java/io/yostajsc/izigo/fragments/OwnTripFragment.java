package io.yostajsc.izigo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.OnConnectionTimeoutListener;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.trip.AddTripActivity;
import io.yostajsc.izigo.activities.trip.TripDetailActivity;
import io.yostajsc.izigo.adapters.TripAdapter;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.trip.Trips;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    private Context mContext = null;
    private TripAdapter tripAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        return rootView;
    }

    private void onApplyViews() {

        mOwnToolbar.setRight(R.drawable.ic_vector_add_group, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup();
            }
        });

        this.tripAdapter = new TripAdapter(mContext);
        UiUtils.onApplyRecyclerView(this.rvTrip, this.tripAdapter, new SlideInUpAnimator(),
                new CallBackWith<Integer>() {
                    @Override
                    public void run(Integer position) {
                        String tripId = tripAdapter.getItem(position).getTripId();
                        Intent intent = new Intent(mContext, TripDetailActivity.class);
                        intent.putExtra(AppDefine.TRIP_ID, tripId);
                        startActivity(intent);
                    }
                });
    }

    private void updateUI(Trips trips) {
        if (trips != null && trips.size() > 0) {
            tripAdapter.replaceAll(trips);
            layoutEmpty.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        onApplyData();
    }

    private void onInternetConnected() {
        String authorization = StorageUtils.inject(mContext)
                .getString(AppDefine.AUTHORIZATION);

        // Load from server
        APIManager.connect().getOwnTripsList(authorization, new CallBack() {
            @Override
            public void run() {
                // TODO: Expired
            }
        }, new CallBackWith<Trips>() {
            @Override
            public void run(Trips trips) {
                RealmManager.insertOrUpdate(trips);
                updateUI(trips);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onApplyData() {

        // Read from disk
        RealmManager.findTrips(new CallBackWith<Trips>() {
            @Override
            public void run(Trips trips) {
                updateUI(trips); // Update UI
            }
        });
        if (NetworkUtils.isNetworkConnected(mContext)) {
            onInternetConnected();
        }
    }

    @OnClick(R.id.button)
    public void addGroup() {
        startActivity(new Intent(getActivity(), AddTripActivity.class));
    }
}
