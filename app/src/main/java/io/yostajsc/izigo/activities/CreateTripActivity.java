package io.yostajsc.izigo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.utils.AppUtils;

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
