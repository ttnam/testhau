package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.ItemClick;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.MemberAdapter;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MembersActivity extends ActivityCoreBehavior {


    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view_curr_member)
    RecyclerView rvMembers;

    private String mTripId;
    private MemberAdapter friendAdapter = null, memberAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        mOwnToolbar.setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onApplyRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        mTripId = intent.getStringExtra(AppDefine.TRIP_ID);
        getMemberList();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String authorization = StorageUtils.inject(MembersActivity.this).getString(AppDefine.AUTHORIZATION);
            String fbToken = token.getToken();
            APIManager.connect().getFriendsList(authorization, fbToken, new CallBackWith<List<Friend>>() {
                @Override
                public void run(List<Friend> friends) {
                    friendAdapter.replaceAll(friends);
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(MembersActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            });
        }
    }

    private void getMemberList() {

        String authorization = StorageUtils.inject(MembersActivity.this).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().getMembers(authorization, mTripId, new CallBack() {
            @Override
            public void run() {
                onExpired();
            }
        }, new CallBackWith<List<Friend>>() {
            @Override
            public void run(List<Friend> friend) {
                memberAdapter.replaceAll(friend);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(MembersActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onApplyRecyclerView() {

        this.friendAdapter = new MemberAdapter(this, false, new ItemClick<Integer, Integer>() {
            @Override
            public void onClick(Integer type, Integer position) {
                if (type == MessageType.ITEM_CLICK_INVITE) {
                    // invites.remove(adapter.getItem(position).getFbId());
                }
                if (type == MessageType.ITEM_CLICK_INVITED) {
                    // invites.add(adapter.getItem(position).getFbId());
                }
            }
        }, null);
        this.rvFriends.setAdapter(friendAdapter);
        this.rvFriends.setHasFixedSize(true);
        this.rvFriends.setItemAnimator(new SlideInUpAnimator());
        this.rvFriends.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvFriends.setNestedScrollingEnabled(false);
        this.rvFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        this.memberAdapter = new MemberAdapter(this, true, new ItemClick<Integer, Integer>() {
            @Override
            public void onClick(Integer type, Integer position) {

            }
        }, new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {
                kick(integer);
            }
        });
        this.rvMembers.setAdapter(memberAdapter);
        this.rvMembers.setHasFixedSize(true);
        this.rvMembers.setItemAnimator(new SlideInUpAnimator());
        this.rvMembers.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvMembers.setNestedScrollingEnabled(false);
        this.rvMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void kick(int pos) {
        String authorization = StorageUtils.inject(MembersActivity.this).getString(AppDefine.AUTHORIZATION);
        APIManager.connect().kick(
                authorization,
                mTripId,
                memberAdapter.getItem(pos).getFbId(), new CallBack() {
                    @Override
                    public void run() {
                        onExpired();
                    }
                }, new CallBack() {
                    @Override
                    public void run() {
                        Toast.makeText(MembersActivity.this, "Bạn đã xoá khỏi group thành công", Toast.LENGTH_SHORT).show();

                        getMemberList();
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String error) {
                        Toast.makeText(MembersActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
  /*  @OnClick(R.id.button_add_friend)
    public void onAddFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image*//*");
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.str_send_invite)));
    }*/

   /* private String makeMembers() {
        String members = "";
        if (invites.size() < 1)
            return members;
        for (int i = 0; i < invites.size(); i++) {
            members += invites.get(i) + ",";
        }
        return members.substring(0, members.length() - 1);
    }*/


}