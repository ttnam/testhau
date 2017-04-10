package io.yostajsc.izigo.activities.core;

import android.content.Intent;

import com.facebook.login.LoginManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.dialogs.DialogNoNet;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CoreActivity;
import io.yostajsc.core.utils.StorageUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.user.LoginActivity;
import io.yostajsc.izigo.configs.AppConfig;

/**
 * Created by nphau on 4/10/17.
 */

public class OwnCoreActivity extends CoreActivity {

    private DialogNoNet mDialogNoNet = null;

    protected CallBack mOnExpiredCallBack = new CallBack() {
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
        mDialogNoNet.setIcon(R.drawable.ic_launcher_sad);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        dismissNoNetDialog();
    }

    protected void onExpired() {
        AppConfig.getInstance().logout();
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
            case MessageType.FROM_GALLERY:
                break;
            case MessageType.ITEM_CLICK_INVITED:
                break;
            case MessageType.LOAD_DONE:
                break;
            case MessageType.PICK_LOCATION_FROM:
                break;
            case MessageType.PICK_LOCATION_TO:
                break;
            case MessageType.TAKE_PHOTO:
                break;
            case MessageType.USER_GPS:
                break;
            case MessageType.GPS_OFF:
                onGpsOff();
                break;
            case MessageType.GPS_ON:
                onGpsOn();
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

    protected void onGpsOn(){

    }

    protected void onGpsOff(){

    }
}
