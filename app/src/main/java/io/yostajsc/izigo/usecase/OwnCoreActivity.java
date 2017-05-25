package io.yostajsc.izigo.usecase;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.user.LoginActivity;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.CoreActivity;
import io.yostajsc.sdk.consts.MessageInfo;
import io.yostajsc.sdk.consts.MessageType;
import io.yostajsc.sdk.dialog.DialogNoNet;

/**
 * Created by nphau on 4/10/17.
 */

public class OwnCoreActivity extends CoreActivity {

    private DialogNoNet mDialogNoNet = null;

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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void expired() {
        AppConfig.getInstance().logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Subscribe
    public void onMessageEvent(MessageInfo info) {
        int code = info.getMessage();
        switch (code) {
            case MessageType.GPS_ON:
                onGpsOn();
                break;
            case MessageType.GPS_OFF:
                onGpsOff();
                break;
            case MessageType.USER_GPS:
                break;
            case MessageType.LOAD_DONE:
                break;
            case MessageType.TAKE_PHOTO:
                break;
            case MessageType.FROM_GALLERY:
                break;
            case MessageType.PICK_LOCATION_TO:
                break;
            case MessageType.PICK_LOCATION_FROM:
                break;
            case MessageType.ITEM_CLICK_INVITED:
                break;
            case MessageType.INTERNET_CONNECTED:
                onInternetConnected();
                break;
            case MessageType.INTERNET_NO_CONNECTED:
                onInternetDisConnected();
                break;
        }
    }

    @Override
    public void onInternetConnected() {
        if (this.mDialogNoNet != null)
            this.mDialogNoNet.dismiss();
    }

    @Override
    public void onInternetDisConnected() {
        if (this.mDialogNoNet == null) {
            this.mDialogNoNet = new DialogNoNet(this);
            this.mDialogNoNet.setIcon(R.drawable.ic_launcher_sad);
        }
        if (this.mDialogNoNet.isShowing())
            return;
        this.mDialogNoNet.show();
    }

    protected void onGpsOn() {

    }

    protected void onGpsOff() {

    }
}
