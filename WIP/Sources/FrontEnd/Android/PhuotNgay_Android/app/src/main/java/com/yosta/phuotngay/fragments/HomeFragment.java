package com.yosta.phuotngay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.ItemTripAdapter;
import com.yosta.phuotngay.helpers.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.models.views.ItemTripView;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private ItemTripAdapter itemTripAdapter = null;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        this.itemTripAdapter = new ItemTripAdapter(container.getContext());

        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(2));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                container.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(itemTripAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );

        /*recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });*/
        itemTripAdapter.clear();
        for (int i = 0; i < 10; i++) {
            itemTripAdapter.addTrip(new ItemTripView());
        }
        /*// RecyclerView Near
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerViewNear.setHasFixedSize(true);
        recyclerViewNear.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNear.addItemDecoration(new DividerItemDecoration(16));
        recyclerViewNear.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerViewNear.setAdapter(itemTripAdapter);
        LinearLayoutManager linearLayoutManagerNear = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNear.setLayoutManager(linearLayoutManagerNear);*/
/*
        recyclerView.setOnFlingListener(new RecyclerViewSwipeListener(true) {
            @Override
            public void onSwipeDown() {
                Toast.makeText(getContext(), "swipe down", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeUp() {
                Toast.makeText(getContext(), "swipe up", Toast.LENGTH_SHORT).show();
            }
        });*/
        return rootView;
    }

    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes
    }
}
