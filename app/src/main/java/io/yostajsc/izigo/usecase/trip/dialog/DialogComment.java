package io.yostajsc.izigo.usecase.trip.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.sdk.consts.CallBackWith;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.sdk.utils.AppUtils;
import io.yostajsc.sdk.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.trip.adapter.CommentAdapter;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgComment;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.izigo.ui.UiUtils;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */

public class DialogComment extends Dialog {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.edit_text)
    AppCompatEditText editText;

    @BindView(R.id.text_trip_name)
    TextView textTripName;

    @BindView(R.id.button)
    AppCompatImageView btnSend;

    private String mTripId = null;
    private Activity mOwnerActivity = null;
    private CommentAdapter mAdapter = null;

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
    }

    private void onApplyRecyclerView() {
        this.mAdapter = new CommentAdapter(mOwnerActivity);
        UiUtils.onApplyRecyclerView(this.recyclerView, mAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
            @Override
            public void run(Integer integer) {

            }
        });
    }

    public void setTripName(String tripName) {
        textTripName.setText(tripName);
    }

    public void setTripId(String tripId) {
        if (TextUtils.isEmpty(tripId))
            return;

        mTripId = tripId;

        IzigoSdk.TripExecutor.getComments(tripId, new IgCallback<List<IgComment>, String>() {
            @Override
            public void onSuccessful(List<IgComment> comments) {
                updateUI(comments);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(getContext(), error);
            }

            @Override
            public void onExpired() {

            }
        });
    }

    private void updateUI(List<IgComment> comments) {
        if (comments.size() > 0) {
            mAdapter.replaceAll(comments);
        }
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        final String content = editText.getText().toString();
        if (TextUtils.isEmpty(content))
            return;
        IgComment igComment = new IgComment(content);

        igComment.setName(IzigoSdk.UserExecutor.getOwnName());
        igComment.setAvatar(IzigoSdk.UserExecutor.getOwnAvatar());

        mAdapter.add(igComment);

        if (!NetworkUtils.isNetworkConnected(mOwnerActivity))
            return;

        IzigoSdk.TripExecutor.addComment(mTripId, content, new CallBackWith<String>() {
            @Override
            public void run(String error) {
                ToastUtils.showToast(mOwnerActivity, error);
            }
        });

        editText.setText("");
        editText.clearFocus();
        AppUtils.closeVirtualKeyboard(mOwnerActivity);
    }
}