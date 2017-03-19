package io.yostajsc.izigo.services.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;

/**
 * Created by Phuc-Hau Nguyen on 7/22/2016.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isNetworkConnected(context)) {
            EventBus.getDefault().post(new MessageInfo(MessageType.INTERNET_CONNECTED));
        } else {
            EventBus.getDefault().post(new MessageInfo(MessageType.LOST_INTERNET));
        }
    }
}
