package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.core.callbacks.CallBack;
import io.yostajsc.core.callbacks.CallBackWith;
import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.designs.animations.YoYo;
import io.yostajsc.designs.animations.fading_entrances.FadeInAnimator;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.izigo.adapters.CommentAdapter;
import io.yostajsc.izigo.models.comment.Comments;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.UiUtils;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */

public class DialogComment extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout)
    RelativeLayout layouNoNet;

    @BindView(R.id.edit_text)
    AppCompatEditText editText;

    @BindView(R.id.button)
    AppCompatImageView btnSend;


    private Activity mOwnerActivity = null;
    private CommentAdapter mCommentsAdapter = null;

    public DialogComment(Context context) {
        super(context, R.style.CoreAppTheme_Dialog);
        mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.CoreAppTheme_AnimDialog_SlideUpDown;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mOwnerActivity = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_comment);
        ButterKnife.bind(this);
        onApplyRecyclerView();
        // onToggleUI(NetworkUtils.isNetworkConnected(mOwnerActivity));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void onApplyRecyclerView() {
        this.mCommentsAdapter = new CommentAdapter(mOwnerActivity);
        UiUtils.onApplyRecyclerView(this.recyclerView, mCommentsAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {

            }
        });
    }

    private void onToggleUI(boolean IsConnected) {
        if (IsConnected) {
            layouNoNet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layouNoNet.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageInfo messageInfo) {
        /*if (messageInfo.getMessage() == MessageType.INTERNET_CONNECTED) {
            layoutRelative.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }*/
    }

    public void setTripId(String tripId) {

        // Load from disk

        // Load from internet
        if (NetworkUtils.isNetworkConnected(mOwnerActivity)) {

            String authorization = StorageUtils.inject(mOwnerActivity).getString(AppDefine.AUTHORIZATION);

            if (ValidateUtils.canUse(authorization)) {
                APIManager.connect().getComments(authorization, tripId, new CallBack() {
                    @Override
                    public void run() {
                        // TODO: expired
                    }
                }, new CallBackWith<Comments>() {
                    @Override
                    public void run(Comments comments) {
                        // TODO: Write to disk

                        updateUI(comments);
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String s) {

                    }
                });
            }
        }
    }

    private void updateUI(Comments comments) {
        if (comments != null && comments.size() > 0) {
            mCommentsAdapter.replaceAll(comments);
            layouNoNet.setVisibility(View.GONE);
        } else {
            layouNoNet.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        final String cmtContent = editText.getText().toString();

        /*if (ValidateUtils.isCommentAccepted(cmtContent)) {

            final String uid = "KGSdIvQ1ESWOJfHPJYqkCeX1juf2";
            final String username = "Nguyễn Phúc Hậu";

            FirebaseManager.inject().USER().child(uid).child("avatar")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                String avatar = (String) dataSnapshot.getValue();

                                FirebaseComment comment = new FirebaseComment();
                                comment.setAvatar(avatar);
                                comment.setUsername(username);
                                comment.setContent(cmtContent);
                                comment.setCreatedtime(System.currentTimeMillis());
                                comment.setUserid(uid);
                                FirebaseManager.inject().COMMENT(mTripId).push().setValue(comment);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }*/
        editText.clearFocus();
        editText.setText("");
        AppUtils.onCloseVirtualKeyboard(mOwnerActivity);
    }

    @OnClick(R.id.layout)
    public void onReload() {
        YoYo.with(new FadeInAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(layouNoNet);
        onToggleUI(NetworkUtils.isNetworkConnected(getContext()));
    }
}