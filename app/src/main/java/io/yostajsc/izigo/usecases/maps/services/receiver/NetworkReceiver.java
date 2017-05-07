package io.yostajsc.izigo.usecases.maps.services.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import io.yostajsc.core.code.MessageInfo;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.utils.maps.MapUtils;

/**
 * Created by Phuc-Hau Nguyen on 7/22/2016.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            if (MapUtils.Gps.connect().isEnable()) {
                EventBus.getDefault().post(new MessageInfo(MessageType.GPS_ON));
            } else {
                EventBus.getDefault().post(new MessageInfo(MessageType.GPS_OFF));
            }
        } else if (NetworkUtils.isNetworkConnected(context)) {
            EventBus.getDefault().post(new MessageInfo(MessageType.INTERNET_CONNECTED));
        } else {
            EventBus.getDefault().post(new MessageInfo(MessageType.INTERNET_NO_CONNECTED));
        }
    }
}
