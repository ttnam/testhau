package io.yostajsc.izigo.activities.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.TimelineAdapter;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.managers.RealmManager;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.utils.NetworkUtils;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.utils.validate.ValidateUtils;
import io.yostajsc.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripTimelineActivity extends ActivityBehavior {


    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTimeline;

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @BindView(R.id.button)
    Button button;

    private String tripId = null;
    private TimelineAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_timeline);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    public void onApplyViews() {
        mOwnToolbar.setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        this.adapter = new TimelineAdapter(this);
        UiUtils.onApplyRecyclerView(this.rvTimeline, this.adapter, new SlideInUpAnimator(),
                new CallBackWith<Integer>() {
                    @Override
                    public void run(Integer position) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        onApplyData();

    }

    @Override
    public void onApplyData() {
        Intent intent = getIntent();
        tripId = intent.getStringExtra(Trip.TRIP_ID);
        if (ValidateUtils.canUse(tripId)) {

            // Load from disk
            loadFromDisk();

            if (NetworkUtils.isNetworkConnected(this)) {
                onInternetConnected();
            }
        }
    }

    private void updateUI(Timelines timelines) {
        if (timelines != null && timelines.size() > 0) {
            layoutEmpty.setVisibility(View.GONE);
            this.adapter.replaceAll(timelines);
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            button.setText(getString(R.string.str_create_new_ac));
        }
    }

    private void loadFromDisk() {
        RealmManager.findActivies(new CallBackWith<Timelines>() {
            @Override
            public void run(Timelines timelines) {
                updateUI(timelines); // Update UI
            }
        });
    }

    @Override
    protected void onInternetConnected() {
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);

        if (ValidateUtils.canUse(authorization)) {

            APIManager.connect().getActivities(authorization, tripId, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            }, new CallBackWith<Timelines>() {
                @Override
                public void run(final Timelines timelines) {
                    RealmManager.insertOrUpdate(timelines);
                    updateUI(timelines);
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(TripTimelineActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onInternetDisconnected() {

    }
}
