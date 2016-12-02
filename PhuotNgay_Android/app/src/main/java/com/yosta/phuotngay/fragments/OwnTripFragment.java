package com.yosta.phuotngay.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.TimelineAdapter;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.models.view.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView rvTimeLine;

    private Activity mActivity;
    private TimelineAdapter mTimelineAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        this.mTimelineAdapter = new TimelineAdapter(mActivity);
        onApplyRecyclerView();
        onApplyData();

        return rootView;
    }

    public void onApplyData() {
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this.mActivity,
                LinearLayoutManager.VERTICAL, false);

        this.rvTimeLine.setNestedScrollingEnabled(false);
        this.rvTimeLine.setLayoutManager(layoutManager);
        this.rvTimeLine.setAdapter(this.mTimelineAdapter);
    }
}
