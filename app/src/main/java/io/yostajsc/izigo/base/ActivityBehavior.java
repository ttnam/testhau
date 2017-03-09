package io.yostajsc.izigo.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.yostajsc.constants.MessageInfo;
import io.yostajsc.constants.MessageType;
import io.yostajsc.izigo.activities.dialogs.DialogNoNet;
import io.yostajsc.izigo.activities.user.LoginActivity;


/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityBehavior extends AppCompatActivity implements ActivityInterface {


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
                onInternetDisconnected();
                break;
        }
    }

    protected void onInternetDisconnected() {
        showNoNetDialog();
    }

    protected void onInternetConnected() {
        dismissNoNetDialog();
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
