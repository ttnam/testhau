package com.yosta.phuotngay.dialogs;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.CommentAdapter;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.fading_entrances.FadeInAnimator;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.app.UIUtils;
import com.yosta.phuotngay.interfaces.DialogBehavior;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.models.comment.Comment;

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

    @BindView(R.id.layout)
    RelativeLayout layoutRelative;

    @BindView(R.id.edit_text)
    AppCompatEditText editText;

    @BindView(R.id.button)
    AppCompatImageView btnSend;


    private CommentAdapter mCommentsAdapter = null;

    private Activity mOwnerActivity = null;

    public DialogComment(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        mCommentsAdapter = new CommentAdapter(context);
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
        onApplyData();
        onApplyEvents();
    }

    @Override
    public void onApplyComponents() {
        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //TODO
        //recyclerView.addItemDecoration(new DividerItemDecoration(2));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getOwnerActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(mCommentsAdapter);
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
            recyclerView.setVisibility(View.VISIBLE);
            onInitializeData();
        } else {
            layoutRelative.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageInfo messageInfo) {
        if (messageInfo.getMessage() == MessageType.INTERNET_CONNECTED) {
            layoutRelative.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            onInitializeData();
        }
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        String cmtContent = editText.getText().toString();
        if (UIUtils.isCommentAccepted(cmtContent)) {

            int index = mCommentsAdapter.addComment(new Comment(cmtContent));
            recyclerView.scrollToPosition(index);

            onCloseVirtualKeyboard();
        }
        editText.clearFocus();
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

    @OnClick(R.id.layout)
    public void onReload() {
        YoYo.with(new FadeInAnimator()).duration(1200)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(layoutRelative);
        onToggleUI(AppUtils.isNetworkConnected(getContext()));
    }

    private void onCloseVirtualKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) mOwnerActivity.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}