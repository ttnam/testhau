package com.yosta.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.yosta.phuotngay.activities.user.LoginActivity;

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
    public void onApplyFont() {

    }

    protected void onExpired() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
