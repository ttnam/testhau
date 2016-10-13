package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.DialogBehavior;
import com.yosta.phuotngay.adapters.CommentAdapter;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.fading_entrances.FadeInAnimator;
import com.yosta.phuotngay.config.message.NetworkMessage;
import com.yosta.phuotngay.globalapp.AppUtils;
import com.yosta.phuotngay.globalapp.UIUtils;
import com.yosta.phuotngay.model.comment.Comment;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogComment extends Dialog implements DialogBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout_relative)
    RelativeLayout layoutRelative;

    @BindView(R.id.layout_recycler)
    RelativeLayout layoutRecycler;

    @BindView(R.id.editText)
    AppCompatEditText editText;

    @BindView(R.id.button_ok)
    AppCompatImageView btnSend;

    private Activity ownerActivity = null;
    private CommentAdapter commentsAdapter = null;

    public DialogComment(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        commentsAdapter = new CommentAdapter(context);
        ownerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (ownerActivity != null)
            setOwnerActivity(ownerActivity);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ownerActivity = getOwnerActivity();
    }

    @Override
    public void onAttachedWindow(Context context) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_comment);
        ButterKnife.bind(this);

        onApplyComponents();
        onApplyData();
        onApplyEvents();
    }

    @Override
    public void onApplyComponents() {
        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(2));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getOwnerActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentsAdapter);
    }

    @Override
    public void onApplyEvents() {

    }

    @Override
    public void onApplyData() {
        onToggleUI(AppUtils.isNetworkConnected(getContext()));
    }

    private void onInitializeData() {

    }

    private void onToggleUI(boolean IsConnected) {
        if (IsConnected) {
            layoutRelative.setVisibility(View.GONE);
            layoutRecycler.setVisibility(View.VISIBLE);
            onInitializeData();
        } else {
            layoutRelative.setVisibility(View.VISIBLE);
            layoutRecycler.setVisibility(View.GONE);
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkMessage networkMessage) {
        if (networkMessage.IsConnected()) {
            layoutRelative.setVisibility(View.GONE);
            layoutRecycler.setVisibility(View.VISIBLE);
            onInitializeData();
        }
    }

    @OnClick(R.id.button_ok)
    public void onSendComment() {
        String cmtContent = editText.getText().toString();
        if (UIUtils.IsCommentAccepted(cmtContent)) {
            int index = commentsAdapter.addComment(new Comment(cmtContent));
            recyclerView.scrollToPosition(index);
        }
        editText.clearFocus();
        //editText.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.layout_relative)
    public void onReload() {
        YoYo.with(new FadeInAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(layoutRelative);
        onToggleUI(AppUtils.isNetworkConnected(getContext()));
    }
}