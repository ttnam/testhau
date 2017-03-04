package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.TripAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseTrips;
import com.yosta.utils.AppUtils;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    private FirebaseTrips trips = null;
    private TripAdapter tripAdapter = null;

    @Override
    protected void onStart() {
        super.onStart();
        onApplyComponents();
        Intent intent = this.getIntent();
        trips = (FirebaseTrips) intent.getSerializableExtra(AppUtils.EXTRA_TRIPS);
        if (trips != null) {
            // this.tripAdapter.adds(trips.getTrips());
        }
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        this.tripAdapter = new TripAdapter(this);

        onInitializeTrip();
    }

    private void onInitializeTrip() {

        this.rvTrip.setHasFixedSize(true);
        this.rvTrip.setNestedScrollingEnabled(false);
        this.rvTrip.setItemAnimator(new SlideInUpAnimator());
        this.rvTrip.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       /* FirebaseTrip trip = tripAdapter.getItem(position);
                        if (trip != null) {
                            Intent intent = new Intent(SearchActivity.this, TripDetailActivity.class);
                            intent.putExtra(AppUtils.EXTRA_TRIP, trip);
                            startActivity(intent);
                        }*/
                    }
                }));
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(3));
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvTrip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.rvTrip.setAdapter(this.tripAdapter);
    }

}
