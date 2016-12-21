package com.yosta.phuotngay.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.fading_entrances.FadeInAnimator;
import com.yosta.phuotngay.firebase.adapter.FirebaseCommentAdapter;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.firebase.model.FirebaseComment;
import com.yosta.phuotngay.helpers.AppHelper;
import com.yosta.phuotngay.helpers.validate.ValidateHelper;
import com.yosta.phuotngay.ui.decoration.SpacesItemDecoration;
import com.yosta.phuotngay.models.app.MessageInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */

public class DialogComment extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout)
    RelativeLayout layoutRelative;

    @BindView(R.id.edit_text)
    AppCompatEditText editText;

    @BindView(R.id.button)
    AppCompatImageView btnSend;


    private String mTripId;
    private Activity mOwnerActivity = null;
    private FirebaseCommentAdapter mCommentsAdapter = null;

    public DialogComment(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
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

        onApplyComponents();
        onToggleUI(AppHelper.isNetworkConnected(mOwnerActivity));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private RecyclerView.LayoutManager layoutManager = null;

    public void onApplyComponents() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new SlideInUpAnimator());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.layoutManager = new LinearLayoutManager(mOwnerActivity, LinearLayoutManager.VERTICAL, true);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    private void onToggleUI(boolean IsConnected) {
        if (IsConnected) {
            layoutRelative.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layoutRelative.setVisibility(View.VISIBLE);
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
        this.mTripId = tripId;
        if (mCommentsAdapter != null) {
            mCommentsAdapter.cleanup();
        }
        mCommentsAdapter = new FirebaseCommentAdapter(FirebaseUtils.getInstance().COMMENTRef(tripId));
        recyclerView.setAdapter(mCommentsAdapter);
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        final String cmtContent = editText.getText().toString();

        /*if (ValidateHelper.isCommentAccepted(cmtContent)) {

            final String uid = "KGSdIvQ1ESWOJfHPJYqkCeX1juf2";
            final String username = "Nguyễn Phúc Hậu";

            FirebaseUtils.getInstance().USER().child(uid).child("avatar")
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
                                FirebaseUtils.getInstance().COMMENT(mTripId).push().setValue(comment);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }*/
        editText.clearFocus();
        editText.setText("");
        AppHelper.onCloseVirtualKeyboard(mOwnerActivity);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.layout)
    public void onReload() {
        YoYo.with(new FadeInAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(layoutRelative);
        onToggleUI(AppHelper.isNetworkConnected(getContext()));
    }
}