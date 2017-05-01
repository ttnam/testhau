package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.AppConfig;
import io.yostajsc.sdk.model.user.IgFriend;
import io.yostajsc.core.utils.PrefsUtils;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.interfaces.ItemClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.MemberAdapter;
import io.yostajsc.ui.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MembersActivity extends OwnCoreActivity {


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
        mTripId = PrefsUtils.inject(this).getString(IgTrip.TRIP_ID);
        getMemberList();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        IzigoSdk.UserExecutor.getFriends(AppConfig.getInstance().getFbToken(), new IGCallback<List<IgFriend>, String>() {
            @Override
            public void onSuccessful(List<IgFriend> friends) {
                friendAdapter.replaceAll(friends);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                mOnExpiredCallBack.run();
            }
        });
    }

    private void getMemberList() {

        IzigoSdk.TripExecutor.getMembers(mTripId, new IGCallback<List<IgFriend>, String>() {
            @Override
            public void onSuccessful(List<IgFriend> friends) {
                memberAdapter.replaceAll(friends);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                mOnExpiredCallBack.run();
            }
        });
    }

    private void addMember(String fbId) {

        IzigoSdk.TripExecutor.addMembers(mTripId, fbId, new IGCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                AppConfig.showToast(MembersActivity.this, "Lời mời đã được gửi đi");
                getMemberList();
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(MembersActivity.this, error);
            }

            @Override
            public void onExpired() {
                mOnExpiredCallBack.run();
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

        IzigoSdk.TripExecutor.kickMembers(mTripId,
                memberAdapter.getItem(pos).getFbId(), new IGCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {
                        AppConfig.showToast(MembersActivity.this, "Bạn đã xoá khỏi group thành công");
                        getMemberList();
                    }

                    @Override
                    public void onFail(String error) {
                        AppConfig.showToast(MembersActivity.this, error);
                    }

                    @Override
                    public void onExpired() {
                        mOnExpiredCallBack.run();
                    }
                });
    }
    @OnClick(R.id.button_add_friend)
    public void onAddFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image");
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.str_send_invite)));
    }

}
