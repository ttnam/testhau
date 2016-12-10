package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.TripAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseTrips;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

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
        Intent intent = this.getIntent();
        trips = (FirebaseTrips)
                intent.getSerializableExtra(AppUtils.EXTRA_TRIPS);
        if (trips != null) {
            this.tripAdapter.adds(trips.getTrips());
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
        this.rvTrip.setItemAnimator(new SlideInUpAnimator());
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(3));

        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2, GridLayoutManager.VERTICAL, false);

        this.rvTrip.setNestedScrollingEnabled(false);
        this.rvTrip.setLayoutManager(layoutManager);
        this.rvTrip.setAdapter(this.tripAdapter);

    }
}
