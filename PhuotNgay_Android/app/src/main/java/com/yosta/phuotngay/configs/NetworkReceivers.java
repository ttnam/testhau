package com.yosta.phuotngay.configs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yosta.phuotngay.configs.message.NetworkMessage;
import com.yosta.phuotngay.helpers.app.AppUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Phuc-Hau Nguyen on 7/22/2016.
 */
public class NetworkReceivers extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppUtils.isNetworkConnected(context)) {
            EventBus.getDefault().post(new NetworkMessage(true));
        }
    }
}
