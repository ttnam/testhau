package io.yostajsc.izigo.usecase.trip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.MainActivity;
import io.yostajsc.izigo.dialogs.DialogFilter;
import io.yostajsc.izigo.usecase.trip.TripDetailActivity;
import io.yostajsc.izigo.usecase.trip.adapter.TripAdapter;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.izigo.utils.UiUtils;
import io.yostajsc.izigo.usecase.customview.OwnToolBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFragment extends CoreFragment {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    @BindView(R.id.layout)
    FrameLayout layout;

    private TripAdapter tripAdapter = null;
    private CallBack mOnExpired = new CallBack() {
        @Override
        public void run() {
            ((MainActivity) getActivity()).expired();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadTripsFromServer();
    }

    private void onApplyViews() {
        mOwnToolbar.setTitle(getString(R.string.all_popular))
                .setOnlyRight(R.drawable.ic_vector_filter_gray, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFilter dialog = new DialogFilter(mContext);
                        dialog.show();
                    }
                });
        this.tripAdapter = new TripAdapter(mContext);
        UiUtils.onApplyRecyclerView(this.rvTrip, this.tripAdapter, new SlideInUpAnimator(),
                new CallBackWith<Integer>() {
                    @Override
                    public void run(Integer position) {
                        final String tripId = tripAdapter.getItem(position).getId();
                        AppConfig.getInstance().setCurrentTripId(tripId);
                        startActivity(new Intent(mContext, TripDetailActivity.class));
                    }
                });
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(5));
    }

    public void loadTripsFromServer() {

        if (NetworkUtils.isNetworkConnected(mContext)) {

            IzigoSdk.TripExecutor.getPublishTrip(new IgCallback<List<IgTrip>, String>() {
                @Override
                public void onSuccessful(List<IgTrip> publicTrips) {
                    processingUiUpdate(publicTrips);
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(mContext, error);
                }

                @Override
                public void onExpired() {
                    mOnExpired.run();
                }
            });
        }
    }

    private void processingUiUpdate(List<IgTrip> publicTrips) {
        int size = publicTrips.size();
        if (size < 0)
            return;
        tripAdapter.clear();
        for (int i = 0; i < size; i++) {
            tripAdapter.add(publicTrips.get(i));
        }
        hideProgress();
    }

    private void hideProgress() {
        layout.setVisibility(View.GONE);
    }
}
