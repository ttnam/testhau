package io.yostajsc.izigo.usecase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.consts.ItemClick;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.api.model.user.IgFriend;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.trip.adapter.MemberAdapter;
import io.yostajsc.izigo.usecase.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MembersActivity extends OwnCoreActivity {

    private static final String TAG = MembersActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view_curr_member)
    RecyclerView rvMembers;

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
        mOwnToolbar.setOnlyLeft(R.drawable.ic_vector_back_blue, new View.OnClickListener() {
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
        getMemberList();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        IzigoSdk.UserExecutor.getFriends(AppConfig.getInstance().getFbToken(), new IgCallback<List<IgFriend>, String>() {
            @Override
            public void onSuccessful(List<IgFriend> friends) {
                friendAdapter.replaceAll(friends);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    private void getMemberList() {

        Log.e(TAG, AppConfig.getInstance().getCurrentTripId());
        IzigoSdk.TripExecutor.getMembers(AppConfig.getInstance().getCurrentTripId(), new IgCallback<List<IgFriend>, String>() {
            @Override
            public void onSuccessful(List<IgFriend> friends) {
                memberAdapter.replaceAll(friends);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    private void addMember(String fbId) {

        IzigoSdk.TripExecutor.addMembers(AppConfig.getInstance().getCurrentTripId(), fbId, new IgCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                ToastUtils.showToast(MembersActivity.this, "Lời mời đã được gửi đi");
                getMemberList();
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                expired();
            }
        });
    }

    private void onApplyRecyclerView() {

        this.friendAdapter = new MemberAdapter(this, false, new ItemClick<Integer, Integer>() {
            @Override
            public void onClick(Integer type, Integer position) {
                if (type == MessageType.ITEM_CLICK_INVITED) {
                    addMember(friendAdapter.getItem(position).getFbId());
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

        IzigoSdk.TripExecutor.kickMembers(AppConfig.getInstance().getCurrentTripId(),
                memberAdapter.getItem(pos).getFbId(), new IgCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        ToastUtils.showToast(MembersActivity.this, "Bạn đã xoá khỏi group thành công");
                        getMemberList();
                    }

                    @Override
                    public void onFail(String error) {
                        ToastUtils.showToast(MembersActivity.this, error);
                    }

                    @Override
                    public void onExpired() {
                        expired();
                    }
                });
    }

    @OnClick(R.id.button_add_friend)
    public void onAddFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getString(R.string.str_send_invite)));
    }

}
