package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.designs.decorations.SpacesItemDecoration;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.TripAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends OwnCoreActivity {

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    // private FirebaseTrips trips = null;
    private TripAdapter tripAdapter = null;

    @Override
    protected void onStart() {
        super.onStart();
        onApplyViews();
        Intent intent = this.getIntent();
      /*  trips = (FirebaseTrips) intent.getSerializableExtra(AppUtils.EXTRA_TRIPS);
        if (trips != null) {
            // this.tripAdapter.adds(trips.getTrips());
        }*/
    }

    @Override
    public void onApplyViews() {
        super.onApplyEvents();
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
