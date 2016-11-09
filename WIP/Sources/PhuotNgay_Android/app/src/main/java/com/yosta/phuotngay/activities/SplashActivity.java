package com.yosta.phuotngay.activities;

import android.support.v7.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogProgress;
import com.yosta.phuotngay.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends ActivityBehavior {


    @BindView(R.id.text_view)
    AppCompatTextView textView;

    private DialogProgress dialogProgress;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        dialogProgress = new DialogProgress(this, false, false);
        dialogProgress.show();
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            textView.setText("Not signed in, launch the Sign In activity");
        } else {
            String mUsername = mFirebaseUser.getDisplayName();
            textView.setText(mUsername);
            /*if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
    }
}
