package com.yosta.phuotngay.activities;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogLogin;
import com.yosta.phuotngay.activities.dialogs.DialogRegister;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.bouncing_entrances.BounceInRightAnimator;
import com.yosta.phuotngay.config.AppConfig;
import com.yosta.phuotngay.helpers.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends ActivityBehavior {

    @BindView(R.id.image)
    AppCompatImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    private DialogLogin loginDialog = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        AppConfig appConfig = (AppConfig) getApplication();
        if (appConfig.IsUserLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            onClose();
        }

        loginDialog = new DialogLogin(this);
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();
        YoYo.with(new BounceInRightAnimator()).duration(900)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(imageView);
    }

    @OnClick(R.id.button_ok)
    public void Login() {
        loginDialog.show();
    }

    @OnClick(R.id.button_no)
    public void Register() {
        DialogRegister dialogRegister = new DialogRegister(this);
        dialogRegister.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginDialog.dismiss();
    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();

        UIUtils.setFont(this, UIUtils.FONT_LATO_ITALIC, textView);

    }
}
