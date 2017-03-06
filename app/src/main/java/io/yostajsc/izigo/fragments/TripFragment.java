package io.yostajsc.izigo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import io.yostajsc.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.dialogs.DialogFilter;
import io.yostajsc.izigo.activities.trip.TripDetailActivity;
import io.yostajsc.izigo.adapters.TripAdapter;
import io.yostajsc.izigo.backend.config.APIManager;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.interfaces.CallBackParam;
import io.yostajsc.izigo.models.trip.BaseTrip;
import io.yostajsc.izigo.ui.bottomsheet.OwnToolBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.utils.StorageUtils;
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

        mOwnToolbar.setRight(R.drawable.ic_vector_filter, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFilter dialog = new DialogFilter(mContext);
                dialog.show();
            }
        });
        onApplyViews();
        onApplyData();
        return rootView;
    }

    private void onApplyViews() {

        this.tripAdapter = new TripAdapter(mContext);

        this.rvTrip.setHasFixedSize(true);
        this.rvTrip.setItemAnimator(new SlideInUpAnimator());
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        this.rvTrip.setNestedScrollingEnabled(false);
        this.rvTrip.setLayoutManager(layoutManager);
        this.rvTrip.setAdapter(this.tripAdapter);
        this.rvTrip.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BaseTrip trip = tripAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), TripDetailActivity.class);
                intent.putExtra(BaseTrip.EXTRA_TRIP, trip);
                startActivity(intent);
            }
        }));
    }

    private void onApplyData() {
        String authorization = StorageUtils.inject(mContext).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().onGetTrips(authorization, new CallBackParam<List<BaseTrip>>() {
            @Override
            public void run(List<BaseTrip> trips) {
                tripAdapter.replaceAll(trips);
            }
        }, new CallBackParam<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
