package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.login.LoginManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.dialogs.DialogNoNet;
import io.yostajsc.core.interfaces.ActivityCoreInterface;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.activities.user.LoginActivity;
import io.yostajsc.izigo.configs.AppConfig;


/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityCoreBehavior extends AppCompatActivity implements ActivityCoreInterface {


    private DialogNoNet mDialogNoNet = null;
    protected CallBack mExpireCallBack = new CallBack() {
        @Override
        public void run() {
            onExpired();
        }
    };

    @Override
    public void onApplyViews() {

    }

    @Override
    public void onApplyEvents() {

    }

    @Override
    public void onApplyData() {

    }

    @Override
    public void onApplyFont() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        this.mDialogNoNet = new DialogNoNet(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        dismissNoNetDialog();
    }

    protected void onExpired() {
        StorageUtils.inject(this).removes(AppConfig.AUTHORIZATION);
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Subscribe
    public void onMessageEvent(MessageInfo info) {
        int code = info.getMessage();
        switch (code) {
            case MessageType.INTERNET_CONNECTED:
                onInternetConnected();
                break;
            case MessageType.LOST_INTERNET:
                onInternetDisConnected();
                break;
        }
    }

    @Override
    public void onInternetConnected() {
        dismissNoNetDialog();
    }

    @Override
    public void onInternetDisConnected() {
        showNoNetDialog();
    }

    private void dismissNoNetDialog() {
        if (this.mDialogNoNet != null)
            this.mDialogNoNet.dismiss();
    }

    private void showNoNetDialog() {
        if (this.mDialogNoNet != null)
            this.mDialogNoNet.show();
    }
}
