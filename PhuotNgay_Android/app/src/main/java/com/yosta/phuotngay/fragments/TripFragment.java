package com.yosta.phuotngay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.TripDetailActivity;
import com.yosta.phuotngay.adapters.FilterAdapter;
import com.yosta.phuotngay.adapters.TripAdapter;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.helpers.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.models.trip.Trip;
import com.yosta.phuotngay.models.view.FilterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout)
    LinearLayout layoutFilter;

    private TripAdapter placeAdapter = null;
    private FilterAdapter filterAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        Context context = container.getContext();
        this.placeAdapter = new TripAdapter(context);
        this.filterAdapter = new FilterAdapter(context);

        onInitializeView(context);
        onInitializeData();

        return rootView;
    }

    private void onInitializeView(Context context) {
        onInitializeTrip(context);

        this.swipeRefreshLayout.setColorSchemeResources(R.color.Red, R.color.Orange, R.color.Pink);
        this.swipeRefreshLayout.setOnRefreshListener(refreshListener);

        onInitializeFilter();
    }

    private void onInitializeTrip(Context context) {
        this.rvTrip.setHasFixedSize(true);
        this.rvTrip.setItemAnimator(new SlideInUpAnimator());
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(3));

        GridLayoutManager layoutManager = new GridLayoutManager(
                context, 2, GridLayoutManager.VERTICAL, false);
        this.rvTrip.setNestedScrollingEnabled(false);
        this.rvTrip.setLayoutManager(layoutManager);
        this.rvTrip.setAdapter(placeAdapter);
        this.rvTrip.addOnItemTouchListener(tripItemClickListener);
    }

    private void onInitializeFilter() {
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(this.rvFilter);

        this.rvFilter.setHasFixedSize(true);
        this.rvFilter.setItemAnimator(new SlideInLeftAnimator());
        this.rvFilter.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvFilter.addItemDecoration(new SpacesItemDecoration(3));
        this.rvFilter.setNestedScrollingEnabled(false);
        this.rvFilter.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        this.rvFilter.setAdapter(filterAdapter);
        this.rvFilter.addOnItemTouchListener(filterItemClickListener);
    }

    private void onInitializeData() {

        placeAdapter.clear();
        for (int i = 0; i < 100; i++) {
            placeAdapter.add(new Trip());
        }

        filterAdapter.clear();
        filterAdapter.add(new FilterView("Tăng dần"));
        filterAdapter.add(new FilterView("Tháng gần nhất"));
        filterAdapter.add(new FilterView("Năm gần nhất"));
        filterAdapter.add(new FilterView("Đánh giá cao nhất"));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private RecyclerItemClickListener tripItemClickListener = new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            startActivity(new Intent(getActivity(), TripDetailActivity.class));
        }
    });

    private RecyclerItemClickListener filterItemClickListener = new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            filterAdapter.remove(position);
            rvFilter.removeViewAt(position);
            if (filterAdapter.getItemCount() <= 0) {
                layoutFilter.setVisibility(View.GONE);
            }
        }
    });
}
