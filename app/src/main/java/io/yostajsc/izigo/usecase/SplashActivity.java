package io.yostajsc.izigo.usecase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.main.MainActivity;
import io.yostajsc.izigo.usecase.main.OwnCoreActivity;
import io.yostajsc.izigo.usecase.user.LoginActivity;
import io.yostajsc.sdk.api.IzigoSdk;

public class SplashActivity extends OwnCoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        onApplyData();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null &&
                        !accessToken.isExpired() &&
                        IzigoSdk.UserExecutor.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000);
    }
}
