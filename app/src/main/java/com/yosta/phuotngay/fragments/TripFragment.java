package com.yosta.phuotngay.fragments;

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

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogFilter;
import com.yosta.phuotngay.activities.trip.TripDetailActivity;
import com.yosta.phuotngay.adapters.TripAdapter;
import com.yosta.phuotngay.configs.AppDefine;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.interfaces.CallBackTripsParam;
import com.yosta.phuotngay.models.trip.BaseTrip;
import com.yosta.phuotngay.services.api.APIManager;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;

import java.util.List;

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
        String authorization = StorageHelper.inject(mContext).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().onGetTrips(authorization, new CallBackTripsParam() {
            @Override
            public void run(List<BaseTrip> trips) {
                tripAdapter.replaceAll(trips);
            }
        }, new CallBackStringParam() {
            @Override
            public void run(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
