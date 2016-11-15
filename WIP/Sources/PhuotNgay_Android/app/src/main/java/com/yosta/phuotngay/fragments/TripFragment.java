package com.yosta.phuotngay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.TripDetailActivity;
import com.yosta.phuotngay.adapters.TripAdapter;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.helpers.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.models.trip.Trip;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripFragment extends Fragment {

    private TripAdapter placeAdapter = null;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        this.placeAdapter = new TripAdapter(container.getContext());

        onInitializeView(container.getContext());
        onInitializeData();

        return rootView;
    }

    private void onInitializeView(Context context) {

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(0));

        GridLayoutManager layoutManager = new GridLayoutManager(
                context, 2, GridLayoutManager.VERTICAL, false);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(placeAdapter);
        this.recyclerView.addOnItemTouchListener(itemClickListener);

        this.swipeRefreshLayout.setColorSchemeResources(R.color.Red, R.color.Orange, R.color.Pink);
        this.swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    private void onInitializeData() {

        placeAdapter.clear();
        for (int i = 0; i < 10; i++) {
            placeAdapter.addPlace(new Trip());
        }
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            startActivity(new Intent(getActivity(), TripDetailActivity.class));
        }
    });
}
