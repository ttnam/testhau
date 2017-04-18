package io.yostajsc.izigo.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.TimelineAdapter;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.usecase.realm.RealmManager;
import io.yostajsc.utils.UiUtils;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TimeLineFragment extends CoreFragment {

    @BindView(R.id.recycler_view)
    RecyclerView rvTimeline;

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    @BindView(R.id.button)
    Button button;

    private String tripId = null;
    private TimelineAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        // onApplyData();
        return rootView;
    }

    private void onApplyViews() {
        this.adapter = new TimelineAdapter(mContext);
        UiUtils.onApplyRecyclerView(this.rvTimeline, this.adapter, new SlideInUpAnimator(),
                new CallBackWith<Integer>() {
                    @Override
                    public void run(Integer position) {

                    }
                });

    }
/*

    private void onApplyData() {
        Intent intent = getIntent();
        tripId = intent.getStringExtra(Trip.TRIP_ID);
        if (ValidateUtils.canUse(tripId)) {

            // Load from disk
            loadFromDisk();

            if (NetworkUtils.isNetworkConnected(this)) {
                onInternetConnected();
            }
        }
        APIManager.connect().getActivities(tripId, new CallBack() {
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
*/

    private void updateUI(Timelines timelines) {
        if (timelines != null && timelines.size() > 0) {
            this.layoutEmpty.setVisibility(View.GONE);
            this.rvTimeline.setVisibility(View.VISIBLE);
            this.adapter.replaceAll(timelines);
        } else {
            this.adapter.clear();
            this.layoutEmpty.setVisibility(View.VISIBLE);
            this.rvTimeline.setVisibility(View.INVISIBLE);
            this.button.setText(getString(R.string.str_create_new_ac));
        }
    }

    private void loadFromDisk() {
        RealmManager.findActivities(new CallBackWith<Timelines>() {
            @Override
            public void run(Timelines timelines) {
                updateUI(timelines); // Update UI
            }
        });
    }

    @OnClick(R.id.button)
    public void addNewActivity() {

    }

}
