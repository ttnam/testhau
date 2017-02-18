package com.yosta.phuotngay.activities.group;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.FriendAdapter;
import com.yosta.phuotngay.adapters.FriendsResAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.decoration.DividerItemDecoration;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.ui.listeners.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AddGroupActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    @BindView(R.id.recycler_view_res)
    RecyclerView rvFriendsRes;

    private FriendAdapter adapter = null;
    private FriendsResAdapter resAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        onApplyComponents();
        onApplyData();
    }

    @Override
    public void onApplyComponents() {
        onApplyRecyclerViewFriends();
        onApplyRecyclerViewFriendsRes();
    }

    @Override
    public void onApplyData() {
        for (int i = 0; i < 10; i++)
            this.adapter.add(new FirebaseFriend());
        for (int i = 0; i < 10; i++)
            this.resAdapter.add(new FirebaseFriend());
    }

    private void onApplyRecyclerViewFriends() {
        this.adapter = new FriendAdapter(this);
        this.rvFriends.setAdapter(adapter);
        this.rvFriends.setHasFixedSize(true);
        this.rvFriends.setItemAnimator(new SlideInUpAnimator());
        this.rvFriends.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvFriends.setNestedScrollingEnabled(false);
        this.rvFriends.setLayoutManager(layoutManager);
        this.rvFriends.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FirebaseFriend friend = adapter.getItem(position);
                resAdapter.add(friend);
                if (resAdapter.getItemCount() > 0) {
                    rvFriendsRes.setVisibility(View.VISIBLE);
                }
                rvFriendsRes.scrollToPosition(resAdapter.getItemCount() - 1);
            }
        }));
    }

    private void onApplyRecyclerViewFriendsRes() {
        this.resAdapter = new FriendsResAdapter(this);
        this.rvFriendsRes.setAdapter(resAdapter);
        this.rvFriendsRes.setHasFixedSize(true);
        this.rvFriendsRes.addItemDecoration(new SpacesItemDecoration(2));
        this.rvFriendsRes.setItemAnimator(new SlideInLeftAnimator());
        this.rvFriendsRes.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.rvFriendsRes.setNestedScrollingEnabled(false);
        this.rvFriendsRes.setLayoutManager(layoutManager);
        this.rvFriendsRes.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                resAdapter.remove(position);
                if (resAdapter.getItemCount() <= 0) {
                    rvFriendsRes.setVisibility(View.GONE);
                }
            }
        }));
    }
}