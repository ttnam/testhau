package com.yosta.phuotngay.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.GroupDetailActivity;
import com.yosta.phuotngay.activities.TripDetailActivity;
import com.yosta.phuotngay.adapters.GroupAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseGroup;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Activity mActivity;
    private GroupAdapter groupAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        this.groupAdapter = new GroupAdapter(mActivity);
        onApplyRecyclerView();
        onApplyData();
        return rootView;
    }

    public void onApplyData() {
        this.groupAdapter.clear();
        for (int i = 0; i < 20; i++) {
            this.groupAdapter.add(new FirebaseGroup());
        }
    }

    private void onApplyRecyclerView() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new SlideInUpAnimator());
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        this.recyclerView.setItemAnimator(new FadeInLeftAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mActivity, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(this.groupAdapter);
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), GroupDetailActivity.class));
            }
        }));
    }
}
