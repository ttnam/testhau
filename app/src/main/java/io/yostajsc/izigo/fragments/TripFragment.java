package io.yostajsc.izigo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.activities.SettingActivity;
import io.yostajsc.izigo.activities.dialogs.DialogFilter;
import io.yostajsc.izigo.activities.trip.TripDetailActivity;
import io.yostajsc.izigo.adapters.TripAdapter;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.realm.trip.PublicTrips;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.view.OwnToolBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFragment extends CoreFragment {

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    private TripAdapter tripAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        final String tripId = tripAdapter.getItem(pos).getId();

        Intent intent = new Intent(mContext, TripDetailActivity.class);
        intent.putExtra(AppConfig.TRIP_ID, tripId);
        startActivity(intent);

        APIManager.connect().trackingViews(tripId, new CallBack() {
            @Override
            public void run() {
                // TODO: expired
            }
        }, new CallBack() {
            @Override
            public void run() {


            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onApplyData() {
        // Check internet
        if (NetworkUtils.isNetworkConnected(mContext)) {
            APIManager.connect().getAllPublicTrips(new CallBackWith<PublicTrips>() {
                @Override
                public void run(PublicTrips publicTrips) {
                    RealmManager.insertOrUpdate(publicTrips);
                    updateUI(publicTrips);
                }
            }, new CallBack() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).onExpired();
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    AppConfig.showToast(mContext, error);
                    loadFromDisk();
                }
            });
        } else {
            loadFromDisk();
        }
    }

    private void loadFromDisk() {
        RealmManager.findAllPublicTrips(new CallBackWith<PublicTrips>() {
            @Override
            public void run(PublicTrips publicTrips) {
                updateUI(publicTrips);
            }
        });
    }

    private void updateUI(PublicTrips publicTrips) {
        int size = publicTrips.size();
        if (size < 0)
            return;
        for (int i = 0; i < size; i++) {
            tripAdapter.add(publicTrips.get(i));
        }
    }
}
