package com.yosta.phuotngay.activities.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Phuc-Hau Nguyen on 8/3/2016.
 */
public class ActivityBehavior extends AppCompatActivity implements ActivityInterface {


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
    public void onBackPressed() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
