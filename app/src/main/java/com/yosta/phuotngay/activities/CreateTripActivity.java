package com.yosta.phuotngay.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.utils.AppUtils;
import com.yosta.interfaces.ActivityBehavior;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTripActivity extends ActivityBehavior {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onApplyViews();
    }

    @Override
    public void onApplyViews() {
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void click(View view) {
        AppUtils.builder().showSnackBarNotify(view, "Click");
    }
}
