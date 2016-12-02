package com.yosta.phuotngay.activities;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import butterknife.ButterKnife;

public class CreateTripActivity extends ActivityBehavior {

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
    }
}
