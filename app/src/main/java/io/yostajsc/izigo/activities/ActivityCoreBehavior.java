package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.dialogs.DialogNoNet;
import io.yostajsc.core.callbacks.ActivityCoreInterface;
import io.yostajsc.izigo.activities.user.LoginActivity;


/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityCoreBehavior extends AppCompatActivity implements ActivityCoreInterface {


    private DialogNoNet dialogNoNet = null;


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
        this.dialogNoNet = new DialogNoNet(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        dismissNoNetDialog();
    }

    protected void onExpired() {
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
        if (this.dialogNoNet != null)
            this.dialogNoNet.dismiss();
    }

    private void showNoNetDialog() {
        if (this.dialogNoNet != null)
            this.dialogNoNet.show();
    }
}
