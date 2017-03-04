package com.yosta.phuotngay.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.backend.config.APIManager;
import com.yosta.interfaces.CallBackStringParam;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.configs.AppDefine;
import com.yosta.phuotngay.firebase.FirebaseManager;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.utils.StorageUtils;
import com.yosta.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends ActivityBehavior {

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(this)
                .load(R.drawable.ic_loading)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = StorageUtils.inject(SplashActivity.this).getUser();
                if (user != null) {
                    onCallToServer(user);
                }
            }
        }, 3000);
    }

    private void onCallToServer(final User user) {
        if (user != null) {
            String email = user.getEmail();
            String fbId = user.getFbId();
            String fireBaseId = user.getFireBaseId();
            String fcm = StorageUtils.inject(this).getString(FirebaseManager.FIRE_BASE_TOKEN);
            APIManager.connect().onLogin(email, fbId, fireBaseId, fcm, new CallBackStringParam() {
                @Override
                public void run(String authorization) {
                    StorageUtils.inject(SplashActivity.this).save(AppDefine.AUTHORIZATION, authorization);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, new CallBackStringParam() {
                @Override
                public void run(String error) {
                    Toast.makeText(SplashActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}