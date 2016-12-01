package com.yosta.phuotngay.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.TimelineAdapter;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.models.trip.Trip;
import com.yosta.phuotngay.models.view.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class CreateTripActivity extends ActivityBehavior {


    @BindView(R.id.recycler_view)
    RecyclerView rvTimeLine;

    private TimelineAdapter mTimelineAdapter = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);

        this.mTimelineAdapter = new TimelineAdapter(this);

        onApplyRecyclerView();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        this.mTimelineAdapter.clear();
        for (int i = 0; i < 100; i++) {
            this.mTimelineAdapter.add(new TimelineView(""));
        }
    }

    private void onApplyRecyclerView() {
        this.rvTimeLine.setHasFixedSize(true);
        this.rvTimeLine.setItemAnimator(new SlideInUpAnimator());
        this.rvTimeLine.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvTimeLine.addItemDecoration(new SpacesItemDecoration(0));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvTimeLine.setNestedScrollingEnabled(false);
        this.rvTimeLine.setLayoutManager(layoutManager);
        this.rvTimeLine.setAdapter(this.mTimelineAdapter);
    }
}
