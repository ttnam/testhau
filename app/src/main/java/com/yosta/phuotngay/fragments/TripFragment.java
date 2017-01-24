package com.yosta.phuotngay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.TripDetailActivity;
import com.yosta.phuotngay.dialogs.DialogFilter;
import com.yosta.phuotngay.adapters.FilterAdapter;
import com.yosta.phuotngay.firebase.adapter.FirebaseTripAdapter;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.ui.view.FilterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private FilterAdapter filterAdapter = null;
    private Context mContext = null;
    private FirebaseTripAdapter tripAdapter = null;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip, container, false);
        ButterKnife.bind(this, rootView);

        this.tripAdapter = new FirebaseTripAdapter(
                FirebaseManager.inject(mContext).TRIPRef());
        this.filterAdapter = new FilterAdapter(mContext);

        onInitializeTrip(mContext);

        this.swipeRefreshLayout.setColorSchemeResources(R.color.Red, R.color.Orange, R.color.Pink);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!AppHelper.isNetworkConnected(mContext))
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        this.swipeRefreshLayout.setRefreshing(true);

        onInitializeFilter();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void onInitializeTrip(Context context) {

        this.rvTrip.setHasFixedSize(true);
        this.rvTrip.setItemAnimator(new SlideInUpAnimator());
        this.rvTrip.addItemDecoration(new SpacesItemDecoration(3));
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        this.rvTrip.setNestedScrollingEnabled(false);
        this.rvTrip.setLayoutManager(layoutManager);
        this.rvTrip.setAdapter(this.tripAdapter);
        this.rvTrip.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FirebaseTrip trip = tripAdapter.getItem(position);
                trip.setTripId(tripAdapter.getRef(position).getKey());
                Intent intent = new Intent(getActivity(), TripDetailActivity.class);
                intent.putExtra(AppHelper.EXTRA_TRIP, trip);

                startActivity(intent);
            }
        }));
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
        this.rvFilter.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                filterAdapter.remove(position);
                rvFilter.removeViewAt(position);
                if (filterAdapter.getItemCount() <= 0) {
                    layoutFilter.setVisibility(View.GONE);
                }
            }
        }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DialogFilter.Filter filter) {
        if (filter != null) {
            this.filterAdapter.clear();
            this.filterAdapter.add(new FilterView(filter.mDuringTime));
            this.filterAdapter.add(new FilterView(filter.mSortBy));
            this.filterAdapter.notifyDataSetChanged();
            this.layoutFilter.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onMessageEvent(MessageInfo info) {
        @MessageType int msg = info.getMessage();
        if (msg == MessageType.LOAD_DONE || msg == MessageType.LOST_INTERNET) {
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }
    }
}
