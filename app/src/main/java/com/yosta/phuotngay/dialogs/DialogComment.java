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
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.firebase.adapter.FirebaseCommentAdapter;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.firebase.model.FirebaseComment;
import com.yosta.phuotngay.helpers.app.AppUtils;
import com.yosta.phuotngay.helpers.app.UIUtils;
import com.yosta.phuotngay.models.app.MessageInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        onToggleUI(AppUtils.isNetworkConnected(getContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private RecyclerView.LayoutManager layoutManager = null;

    public void onApplyComponents() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    private String mTripId;

    public void setTripId(String tripId) {
        this.mTripId = tripId;
        if (mCommentsAdapter != null) {
            mCommentsAdapter.cleanup();
        }
        mCommentsAdapter = new FirebaseCommentAdapter(FirebaseUtils.initializeWith(mOwnerActivity).COMMENTRef(tripId));
        mCommentsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
/*

                int friendlyMessageCount = mCommentsAdapter.getItemCount();
                int lastVisiblePosition =
                        layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
*/

            }
        });
        recyclerView.setAdapter(mCommentsAdapter);
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        String cmtContent = editText.getText().toString();
        if (UIUtils.isCommentAccepted(cmtContent)) {

            FirebaseComment comment = new FirebaseComment();
            comment.setUsername("Nguyễn Phúc Hậu");
            comment.setContent(cmtContent);
            comment.setCreatedtime(System.currentTimeMillis());

            FirebaseUtils.initializeWith(mOwnerActivity)
                    .COMMENT(mTripId).push().setValue(comment);

            onCloseVirtualKeyboard();
        }
        editText.clearFocus();
        editText.setText("");
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*
        @OnClick(R.id.layout)
        public void onReload() {
            YoYo.with(new FadeInAnimator()).duration(1200)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(layoutRelative);
            onToggleUI(AppUtils.isNetworkConnected(getContext()));
        }
        */
    private void onCloseVirtualKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) mOwnerActivity.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}