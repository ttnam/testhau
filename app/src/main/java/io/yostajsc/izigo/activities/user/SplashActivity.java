package io.yostajsc.izigo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.activities.core.OwnCoreActivity;
import io.yostajsc.usecase.session.SessionManager;

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

                switch (SessionManager.isExpired()) {
                    case SessionManager.TOKEN.APP_TOKEN_EXPIRED:
                    case SessionManager.TOKEN.FACEBOOK_TOKEN_EXPIRED:
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case SessionManager.TOKEN.STILL_LIVE:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        break;
                }
            }
        }, 1000);
    }
}
