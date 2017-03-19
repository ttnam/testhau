package io.yostajsc.core.listeners;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.yostajsc.core.callbacks.ActivityCoreInterface;

/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityCoreBehavior extends AppCompatActivity implements ActivityCoreInterface {


    private final static String TAG = ActivityCoreBehavior.class.getSimpleName();

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
    public void onInternetConnected() {
        Log.d(TAG, "Connected!");
    }

    @Override
    public void onInternetDisConnected() {
        Log.d(TAG, "Disconnected!");
    }


}
