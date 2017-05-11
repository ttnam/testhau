package io.yostajsc.izigo.usecase.trip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.main.MainActivity;
import io.yostajsc.izigo.usecase.trip.AddTripActivity;
import io.yostajsc.izigo.usecase.trip.TripDetailActivity;
import io.yostajsc.izigo.usecase.trip.adapter.TripAdapter;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.izigo.utils.UiUtils;
import io.yostajsc.izigo.usecase.customview.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OwnTripFragment extends CoreFragment {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    private TripAdapter tripAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        onApplyData();
        return rootView;
    }

    private void onApplyViews() {

        mOwnToolbar.setOnlyRight(R.drawable.ic_vector_add_group, new View.OnClickListener() {
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
                        String tripId = tripAdapter.getItem(position).getId();
                        Intent intent = new Intent(mContext, TripDetailActivity.class);
                        intent.putExtra(IgTrip.TRIP_ID, tripId);
                        startActivity(intent);
                    }
                });
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(5));
    }

    private void updateUI(List<IgTrip> trips) {
        try {
            int size = trips.size();
            if (size > 0) {
                layoutEmpty.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    tripAdapter.add(trips.get(i));
                }
            } else {
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromServer() {

        IzigoSdk.TripExecutor.getOwnTrip(new IgCallback<List<IgTrip>, String>() {
            @Override
            public void onSuccessful(List<IgTrip> trips) {
                updateUI(trips);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(mContext, error);
            }

            @Override
            public void onExpired() {
                ((MainActivity) getActivity()).expired();
            }
        });
    }

    private void onApplyData() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            loadFromServer();
        }
    }

    @OnClick(R.id.button)
    public void addGroup() {
        startActivity(new Intent(getActivity(), AddTripActivity.class));
    }
}
