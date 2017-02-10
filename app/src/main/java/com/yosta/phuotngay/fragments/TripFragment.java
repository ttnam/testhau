package com.yosta.phuotngay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.activities.TripDetailActivity;
import com.yosta.phuotngay.adapters.FilterAdapter;
import com.yosta.phuotngay.dialogs.DialogFilter;
import com.yosta.phuotngay.firebase.adapter.FirebaseTripAdapter;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TripFragment extends Fragment {

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView rvTrip;

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

        mOwnToolbar.setTitle("").setRight(R.drawable.ic_vector_filter, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFilter dialog = new DialogFilter(mContext);
                dialog.show();
            }
        });

        this.tripAdapter = new FirebaseTripAdapter(FirebaseManager.inject(mContext).TRIPRef());

        onInitializeTrip(mContext);

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
        this.rvTrip.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
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

    @Subscribe
    public void onMessageEvent(MessageInfo info) {
        @MessageType int msg = info.getMessage();
    }
}
