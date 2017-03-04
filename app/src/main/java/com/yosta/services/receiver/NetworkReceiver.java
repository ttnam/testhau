package com.yosta.services.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yosta.utils.AppUtils;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Phuc-Hau Nguyen on 7/22/2016.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppUtils.isNetworkConnected(context)) {
            EventBus.getDefault().post(new MessageInfo(MessageType.INTERNET_CONNECTED));
        } else {
            EventBus.getDefault().post(new MessageInfo(MessageType.LOST_INTERNET));
        }
    }
}
