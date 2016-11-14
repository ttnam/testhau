package com.yosta.phuotngay.fragments;

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
import com.yosta.phuotngay.adapters.PlaceAdapter;
import com.yosta.phuotngay.helpers.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.helpers.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.models.place.Place;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private PlaceAdapter placeAdapter = null;


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
        this.placeAdapter = new PlaceAdapter(container.getContext());

        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));

        GridLayoutManager layoutManager = new GridLayoutManager(
                container.getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(placeAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // startActivity(new Intent(getActivity(), PlaceDetailActivity.class));
                    }
                })
        );
        placeAdapter.clear();
        for (int i = 0; i < 10; i++) {
            placeAdapter.addPlace(new Place());
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.Red, R.color.Orange, R.color.Pink);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if (!AppUtils.isNetworkConnected(getActivity())) {
                    swipeRefreshLayout.setRefreshing(false);
                }*/
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }
}
