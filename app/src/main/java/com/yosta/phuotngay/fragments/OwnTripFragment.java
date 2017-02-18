package com.yosta.phuotngay.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.group.GroupDetailActivity;
import com.yosta.phuotngay.adapters.GroupAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseGroup;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context mContext = null;
    private GroupAdapter groupAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        this.groupAdapter = new GroupAdapter(mContext);

        mOwnToolbar.setRight(R.drawable.ic_vector_add_group, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        onApplyTabLayout();

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
        this.recyclerView.setItemAnimator(new SlideInLeftAnimator());
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(this.groupAdapter);
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), GroupDetailActivity.class));
            }
        }));
    }

    private void onApplyTabLayout() {
        this.mTabLayout.setSmoothScrollingEnabled(true);
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.all_your)));
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.all_friends)));
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.all_suggestions)));

        this.mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                groupAdapter.clear();
                int k = 10;
                switch (pos) {
                    case 0:
                        k = 13;
                        break;
                    case 1:
                        k = 20;
                        break;
                    case 2:
                        k = 30;
                        break;
                }
                for (int i = 0; i < k; i++) {
                    groupAdapter.add(new FirebaseGroup());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
