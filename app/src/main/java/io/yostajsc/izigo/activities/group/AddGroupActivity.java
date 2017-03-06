
package io.yostajsc.izigo.activities.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.constants.MessageType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.FriendAdapter;
import io.yostajsc.backend.config.APIManager;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.interfaces.CallBack;
import io.yostajsc.izigo.interfaces.CallBackParam;
import io.yostajsc.izigo.interfaces.ItemClick;
import io.yostajsc.izigo.models.Friend;
import io.yostajsc.izigo.ui.bottomsheet.OwnToolBar;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.ValidateUtils;
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
    private List<String> invites = null;

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

        invites = new ArrayList<>();

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String authorization = StorageUtils.inject(AddGroupActivity.this).getString(AppDefine.AUTHORIZATION);
            String fbToken = token.getToken();
            APIManager.connect().getFriendsList(authorization, fbToken, new CallBackParam<List<Friend>>() {
                @Override
                public void run(List<Friend> friends) {
                    adapter.replaceAll(friends);
                }
            }, new CallBackParam<String>() {
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

        this.adapter = new FriendAdapter(this, new ItemClick<Integer, Integer>() {
            @Override
            public void onClick(Integer type, Integer position) {
                if (type == MessageType.ITEM_CLICK_INVITE) {
                    invites.remove(adapter.getItem(position).getFbId());
                }
                if (type == MessageType.ITEM_CLICK_INVITED) {
                    invites.add(adapter.getItem(position).getFbId());
                }
                for (int i = 0; i < invites.size(); i++) {
                    Log.e("Invites", invites.get(i));
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

    private String makeMembers() {
        String members = "";
        if (invites.size() < 1)
            return members;
        for (int i = 0; i < invites.size(); i++) {
            members += invites.get(i) + ",";
        }
        return members.substring(0, members.length() - 1);
    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {
        String groupName = textGroupName.getText().toString();
        if (!ValidateUtils.canUse(groupName)) {
            textGroupName.setError(getString(R.string.error_message_empty));
            return;
        }
        if (invites.size() < 1) {
            Toast.makeText(this, getString(R.string.str_pick_friends), Toast.LENGTH_SHORT).show();
            return;
        }
        String member = makeMembers();
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().createGroup(authorization, groupName, "avatar", "info", member, new CallBack() {
            @Override
            public void run() {
                onExpired();
            }
        }, new CallBackParam<String>() {
            @Override
            public void run(String groupId) {
                onSuccess(groupId);
            }
        }, new CallBackParam<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(AddGroupActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSuccess(String groupId) {
        StorageUtils.inject(AddGroupActivity.this).save(AppDefine.GROUP_ID, groupId);
        startActivity(new Intent(AddGroupActivity.this, GroupDetailActivity.class));
        finish();
    }
}