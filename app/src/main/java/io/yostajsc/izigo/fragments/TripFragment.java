package io.yostajsc.izigo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.callbacks.CallBack;
import io.yostajsc.core.callbacks.CallBackWith;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.SettingActivity;
import io.yostajsc.izigo.activities.dialogs.DialogFilter;
import io.yostajsc.izigo.activities.trip.TripDetailActivity;
import io.yostajsc.izigo.adapters.TripAdapter;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.trip.Trips;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.view.OwnToolBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFragment extends Fragment {

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    private Context mContext = null;
    private TripAdapter tripAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        onApplyData();
        return rootView;
    }

    private void onApplyViews() {

        mOwnToolbar.setTitle(getString(R.string.all_popular))
                .setBinding(R.drawable.ic_vector_filter, R.drawable.ic_tab_menu_selected, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFilter dialog = new DialogFilter(mContext);
                        dialog.show();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, SettingActivity.class));
                    }
                });

        this.tripAdapter = new TripAdapter(mContext);
        UiUtils.onApplyRecyclerView(this.rvTrip, this.tripAdapter, new SlideInUpAnimator(),
                new CallBackWith<Integer>() {
                    @Override
                    public void run(Integer position) {
                        onTripItemClick(position);
                    }
                });
    }

    private void onTripItemClick(int pos) {
        final String tripId = tripAdapter.getItem(pos).getTripId();

        String authorization = StorageUtils.inject(mContext)
                .getString(AppDefine.AUTHORIZATION);

        APIManager.connect().updateView(authorization, tripId, new CallBack() {
            @Override
            public void run() {
                // TODO: expired
            }
        }, new CallBack() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, TripDetailActivity.class);
                intent.putExtra(AppDefine.TRIP_ID, tripId);
                startActivity(intent);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(mContext, TripDetailActivity.class);
        intent.putExtra(AppDefine.TRIP_ID, tripId);
        startActivity(intent);
    }

    private void updateUI(Trips trips) {
        tripAdapter.replaceAll(trips);
    }

    private void onInternetConnected() {
        String authorization = StorageUtils.inject(mContext)
                .getString(AppDefine.AUTHORIZATION);

        // Load from server
        APIManager.connect().getTripsList(authorization, new CallBack() {
            @Override
            public void run() {
                // TODO: Expired
            }
        }, new CallBackWith<Trips>() {
            @Override
            public void run(final Trips trips) {
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
        loadFromDisk();

        if (NetworkUtils.isNetworkConnected(mContext)) {
            onInternetConnected();
        }
    }

    private void loadFromDisk() {
        RealmManager.findTrips(new CallBackWith<Trips>() {
            @Override
            public void run(Trips trips) {
                // Update UI
                updateUI(trips);
            }
        });
    }
}
