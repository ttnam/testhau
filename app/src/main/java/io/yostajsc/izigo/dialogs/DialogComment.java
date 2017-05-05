package io.yostajsc.izigo.dialogs;

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
import io.yostajsc.AppConfig;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.AppUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.CommentAdapter;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.model.Comment;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.utils.UiUtils;
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
    }

    private void onApplyRecyclerView() {
        this.mCommentsAdapter = new CommentAdapter(mOwnerActivity);
        UiUtils.onApplyRecyclerView(this.recyclerView, mCommentsAdapter, new SlideInUpAnimator(), new CallBackWith<Integer>() {
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
        IzigoSdk.TripExecutor.getComments(tripId, new IgCallback<List<Comment>, String>() {
            @Override
            public void onSuccessful(List<Comment> comments) {
                updateUI(comments);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(getContext(), error);
            }

            @Override
            public void onExpired() {

            }
        });
    }

    private void updateUI(List<Comment> comments) {
        if (comments.size() > 0) {
            mCommentsAdapter.replaceAll(comments);
        }
    }

    @OnClick(R.id.button)
    public void onSendComment() {
        final String cmtContent = editText.getText().toString();

        if (ValidateUtils.isCommentAccepted(cmtContent)) {

            final String uid = "KGSdIvQ1ESWOJfHPJYqkCeX1juf2";
            final String username = "Nguyễn Phúc Hậu";

            mCommentsAdapter.add(new Comment());

           /* FirebaseManager.bind().USER().child(uid).child("avatar")
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
                                FirebaseManager.bind().COMMENT(mTripId).push().setValue(comment);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/
        }
        editText.clearFocus();
        editText.setText("");
        AppUtils.closeVirtualKeyboard(mOwnerActivity);
    }
}