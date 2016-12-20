package com.yosta.phuotngay.interfaces;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityBehavior extends AppCompatActivity implements ActivityInterface {

    @Override
    public void onApplyComponents() {

    }

    @Override
    public void onApplyEvents() {

    }
    @Override
    public void onApplyData() {

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
