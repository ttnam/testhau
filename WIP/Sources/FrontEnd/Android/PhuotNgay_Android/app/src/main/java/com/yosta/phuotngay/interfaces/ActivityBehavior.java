package com.yosta.phuotngay.interfaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityBehavior extends AppCompatActivity implements ActivityInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onApplyComponents();
        onApplyFont();
        onApplyEvents();
        onApplyData();
        onRegisterListener();
    }

    @Override
    public void onApplyComponents() {

    }

    @Override
    public void onApplyEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        onApplyData();
    }

    @Override
    public void onApplyData() {

    }

    @Override
    public void onRegisterListener() {
        // EventBus.getDefault().register(this);
    }

    @Override
    public void onUnRegisterListener() {
        // EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onUnRegisterListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClose();
    }

    @Override
    public void onClose() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onApplyFont() {

    }
}
