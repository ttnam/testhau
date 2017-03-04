package com.yosta.phuotngay.activities.group;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.FriendAdapter;
import com.yosta.phuotngay.firebase.model.FirebaseFriend;
import com.yosta.interfaces.ActivityBehavior;
import com.yosta.interfaces.ItemClickCallBack;
import com.yosta.phuotngay.models.app.MessageType;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AddGroupActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    private FriendAdapter adapter = null;

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
    }

    @Override
    public void onApplyData() {
        for (int i = 0; i < 10; i++)
            this.adapter.add(new FirebaseFriend());
    }

    private void onApplyRecyclerViewFriends() {
        this.adapter = new FriendAdapter(this, new ItemClickCallBack() {
            @Override
            public void onClick(int type, int position) {
                if (type == MessageType.ITEM_CLICK_INVITED) {

                }
                if (type == MessageType.ITEM_CLICK_INVITE) {
                    long id = adapter.getItem(position).getId();

                }
            }
        });
        this.rvFriends.setAdapter(adapter);
        this.rvFriends.setHasFixedSize(true);
        this.rvFriends.setItemAnimator(new SlideInUpAnimator());
        this.rvFriends.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvFriends.setNestedScrollingEnabled(false);
        this.rvFriends.setLayoutManager(layoutManager);
    }
}