package com.yosta.phuotngay.activities.group;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.yosta.backend.config.APIManager;
import com.yosta.interfaces.CallBack;
import com.yosta.interfaces.CallBackFriendsParam;
import com.yosta.interfaces.CallBackStringParam;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.FriendAdapter;
import com.yosta.phuotngay.configs.AppDefine;
import com.yosta.firebase.model.FirebaseFriend;
import com.yosta.interfaces.ActivityBehavior;
import com.yosta.interfaces.ItemClickCallBack;
import com.yosta.phuotngay.models.Friend;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.utils.StorageUtils;
import com.yosta.utils.validate.ValidateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AddGroupActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.text_group_name)
    EditText textGroupName;

    private FriendAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {

        mOwnToolbar.setTitle(getString(R.string.str_create_group)).setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        onApplyRecyclerViewFriends();
    }

    @Override
    public void onApplyData() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String authorization = StorageUtils.inject(AddGroupActivity.this).getString(AppDefine.AUTHORIZATION);
            String fbToken = token.getToken();
            APIManager.connect().getFriendsList(authorization, fbToken, new CallBackFriendsParam() {
                @Override
                public void run(List<Friend> friends) {
                    adapter.replaceAll(friends);
                }
            }, new CallBackStringParam() {
                @Override
                public void run(String error) {
                    Toast.makeText(AddGroupActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            });
        }
    }

    private void onApplyRecyclerViewFriends() {
        this.adapter = new FriendAdapter(this, new ItemClickCallBack() {
            @Override
            public void onClick(int type, int position) {
                if (type == MessageType.ITEM_CLICK_INVITED) {

                }
                if (type == MessageType.ITEM_CLICK_INVITE) {

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

    @OnClick(R.id.button_add_friend)
    public void onAddFriends() {

    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {
        String groupName = textGroupName.getText().toString();
        if (ValidateUtils.canUse(groupName)) {

        } else {
            textGroupName.setError(getString(R.string.error_message_empty));
        }
    }
}