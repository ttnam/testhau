
package io.yostajsc.izigo.activities.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.constants.MessageType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.adapters.FriendAdapter;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.interfaces.ItemClick;
import io.yostajsc.izigo.managers.EventManager;
import io.yostajsc.izigo.models.user.Friend;
import io.yostajsc.utils.validate.ValidateUtils;
import io.yostajsc.utils.StorageUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AddTripActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView rvFriends;

    @BindView(R.id.text_view_trip_name)
    TextInputEditText textGroupName;

    @BindView(R.id.switch_publish)
    Switch switchPublish;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    private FriendAdapter adapter = null;
    private List<String> invites = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        onApplyRecyclerViewFriends();

        switchPublish.setOnCheckedChangeListener(EventManager.connect().addCheckedChangeListener(new CallBackWith<Boolean>() {
            @Override
            public void run(Boolean aBoolean) {
                if (aBoolean)
                    Toast.makeText(AddTripActivity.this, "Bạn đã bật chế độ công khai", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddTripActivity.this, "Bạn đã tắt chế độ công khai", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onApplyData() {

        invites = new ArrayList<>();

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String authorization = StorageUtils.inject(AddTripActivity.this).getString(AppDefine.AUTHORIZATION);
            String fbToken = token.getToken();
            APIManager.connect().getFriendsList(authorization, fbToken, new CallBackWith<List<Friend>>() {
                @Override
                public void run(List<Friend> friends) {
                    adapter.replaceAll(friends);
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(AddTripActivity.this, error, Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.image_view)
    public void pickTransfer() {
        DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
        dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
            @Override
            public void run(@TransferType Integer integer) {

                switch (integer) {
                    case TransferType.BUS:
                        imageView.setImageResource(R.drawable.ic_vector_bus);
                        break;
                    case TransferType.MOTORBIKE:
                        imageView.setImageResource(R.drawable.ic_vector_motor_bike);
                        break;
                    case TransferType.WALK:
                        imageView.setImageResource(R.drawable.ic_vector_walk);
                        break;
                }
            }
        });
        dialogPickTransfer.show();
    }

    @OnClick(R.id.button_add_friend)
    public void onAddFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.str_send_invite)));
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


    @OnClick(R.id.button)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        }, new CallBackWith<String>() {
            @Override
            public void run(String groupId) {
                onSuccess(groupId);
            }
        }, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                Toast.makeText(AddTripActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSuccess(String groupId) {
        StorageUtils.inject(AddTripActivity.this).save(AppDefine.GROUP_ID, groupId);
        startActivity(new Intent(AddTripActivity.this, GroupDetailActivity.class));
        finish();
    }

    @Override
    protected void onInternetDisconnected() {

    }

    @Override
    protected void onInternetConnected() {

    }

}